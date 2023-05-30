package org.alword.command;

import org.alword.BalanceEconomy;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

public class Village implements Listener {
    private final FileConfiguration config;
    private final BalanceEconomy economy;
    private final String villagerEmeraldAmountName = "villagerEmeraldAmount";
    private int villagerEmeraldAmount;

    public int getVillagerEmeraldAmount() {
        return villagerEmeraldAmount;
    }

    public Village(BalanceEconomy plugin) {
        economy = plugin;
        economy.getServer().getPluginManager().registerEvents(this, plugin);
        config = economy.getConfig();
        config.options().copyDefaults(true);
        config.addDefault(villagerEmeraldAmountName, true);
        villagerEmeraldAmount = config.getInt(villagerEmeraldAmountName);
    }

    @EventHandler
    public void onTrade(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.MERCHANT) return;
        if (event.getSlotType() != InventoryType.SlotType.RESULT) return;
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) return;
        MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
        ItemStack[] merchantContent = merchantInventory.getContents();
        Merchant merchant = merchantInventory.getMerchant();
        InventoryHolder holder = merchantInventory.getHolder();
        List<MerchantRecipe> merchantRecipes = merchant.getRecipes();
        MerchantRecipe trade = findTrade(currentItem, merchantRecipes);
        if (trade == null) return;
        ItemStack[] tradePrices = trade.getIngredients().toArray(new ItemStack[0]);
        ItemStack price1 = trade.getAdjustedIngredient1();
        if (price1 == null) return;
        ItemStack price2 = tradePrices.length > 1 ? tradePrices[1] : null;
        ItemStack priceSlot1 = merchantContent[0];
        ItemStack priceSlot2 = merchantContent.length > 1 ? merchantContent[1] : null;
        Inventory playerInventory = event.getWhoClicked().getInventory();
        ItemStack product = trade.getResult();
        boolean isShift = event.isShiftClick();
        boolean isEmerald = product.getType() == Material.EMERALD;
        int quantity = 0;
        while (true) {
            boolean hasSecondPrice = false;
            int currentAmount = (quantity + 1) * product.getAmount();
            // учитываем цену только если забираем у жителей изумруды
            if (isEmerald && currentAmount > villagerEmeraldAmount) {
                playerInventory.close();
                break;
            }

            if (priceSlot1 == null || priceSlot1.getAmount() < price1.getAmount()) break;
            if (price2 != null && price2.getType() != Material.AIR) {
                if (priceSlot2 == null) break;
                if (priceSlot2.getAmount() < price2.getAmount()) break;
                hasSecondPrice = true;
            }

            if(isShift){
                Map<Integer, ItemStack> map = playerInventory.addItem(product);
                if (!map.isEmpty()) {
                    ItemStack credit = new ItemStack(product.getType());
                    credit.setAmount(product.getAmount() - map.get(0).getAmount());
                    playerInventory.removeItem(credit);
                    break;
                }
                priceSlot1.setAmount(priceSlot1.getAmount() - price1.getAmount());
                if (hasSecondPrice) priceSlot2.setAmount(priceSlot2.getAmount() - price2.getAmount());
                quantity += 1;
            }else {
                quantity = 1;
                break;
            }
        }
        int emeraldsCount = 0;
        if (price1.getType() == Material.EMERALD) {
            emeraldsCount = price1.getAmount() * quantity;
        }
        if (product.getType() == Material.EMERALD) {
            emeraldsCount = -product.getAmount() * quantity;
        }
        changeGlobalBalance(emeraldsCount);
        updateLevel(holder, emeraldsCount);
        updatePrices(merchant);
        InventoryAction action = event.getAction();
        if(action == InventoryAction.MOVE_TO_OTHER_INVENTORY){
            event.setCancelled(true);
        }else{
            event.setCurrentItem(new ItemStack(Material.AIR));
        }
    }

    private void updateLevel(InventoryHolder holder, int emeraldsCount) {
        if (!(holder instanceof Villager)) return;
        Villager villager = (Villager) holder;
        int experience = villager.getVillagerExperience();
        villager.setVillagerExperience(Math.abs(emeraldsCount) + experience);
    }

    @Nullable
    private MerchantRecipe findTrade(ItemStack currentItem, List<MerchantRecipe> merchantRecipes) {
        MerchantRecipe trade = null;
        for (MerchantRecipe recipe : merchantRecipes) {
            if (recipe.getResult().getType() == currentItem.getType()) {
                trade = recipe;
                break;
            }
        }
        return trade;
    }

    @EventHandler
    public void onVillagerTrade(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.MERCHANT) {
            return;
        }
        MerchantInventory villagerInventory = (MerchantInventory) event.getInventory();
        updatePrices(villagerInventory.getMerchant());
    }

    private void changeGlobalBalance(int amount) {
        villagerEmeraldAmount += amount;
        config.set(villagerEmeraldAmountName, villagerEmeraldAmount);
        economy.saveConfig();
    }

    private void updatePrices(Merchant merchant) {
        List<MerchantRecipe> recipeList = merchant.getRecipes();
        for (MerchantRecipe recipe : recipeList) {
            ItemStack result = recipe.getResult();
            recipe.setMaxUses(Integer.MAX_VALUE);
            recipe.setUses(0);
            HumanEntity humanEntity = merchant.getTrader();
            if (humanEntity == null) return;
            if (result.getType() == Material.EMERALD && result.getAmount() > villagerEmeraldAmount) {
                recipe.setMaxUses(0);
                merchant.getTrader().closeInventory();
            }
        }
        merchant.setRecipes(recipeList);
    }
}
