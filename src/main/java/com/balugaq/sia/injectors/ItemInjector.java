package com.balugaq.sia.injectors;

import com.balugaq.sia.SlimefunItemAlias;
import com.balugaq.sia.interfaces.Compatible;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ItemInjector {

    @SneakyThrows
    public static void injectAll() {
        for (var group : SlimefunItemAlias.getInstance().getConfigManager().getInjects()) {
            var item = Utils.getExactItem(group);
            if (item != null) {
                var newItem = implementDistinctive(item, new DistinctiveItem() {
                    // Apply method
                    @Override
                    public boolean canStack(@NotNull ItemMeta m1, @NotNull ItemMeta m2) {
                        var id1 = m1.getPersistentDataContainer().get(Slimefun.getItemDataService().getKey(), PersistentDataType.STRING);
                        var id2 = m2.getPersistentDataContainer().get(Slimefun.getItemDataService().getKey(), PersistentDataType.STRING);
                        return group.contains(id1) && group.contains(id2);
                    }

                    @Override
                    public @NotNull String getId() {
                        return item.getId();
                    }
                });

                var groupC = new ArrayList<>(group);
                for (var id : Slimefun.getRegistry().getSlimefunItemIds().keySet()) {
                    groupC.remove(id);
                }

                for (var id : groupC) {
                    var fixed = fixedId(newItem, id);
                    Utils.addFixedItem(id, fixed);
                    Slimefun.getRegistry().getAllSlimefunItems().add(fixed);
                    Slimefun.getRegistry().getSlimefunItemIds().put(id, fixed);
                }
            }
        }
    }

    public static SlimefunItem fixedId(SlimefunItem item, String id) {
        return new SlimefunItem(item.getItemGroup(), new SlimefunItemStack(id, item.getItem()), item.getRecipeType(), item.getRecipe(), item.getRecipeOutput() == item.getItem() ? new SlimefunItemStack(id, item.getItem()) : item.getRecipeOutput());
    }

    public static SlimefunItem implementDistinctive(SlimefunItem original, DistinctiveItem distinctive) throws Exception {
        return new ByteBuddy()
                .subclass(original.getClass())
                .implement(DistinctiveItem.class)
                .implement(Compatible.class)
                .method(ElementMatchers.named("canStack"))
                .intercept(InvocationHandlerAdapter.of(new StackHandler(original, distinctive)))
                .make()
                .load(original.getClass().getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public static class StackHandler implements InvocationHandler {
        public final SlimefunItem original;
        public final DistinctiveItem distinctive;

        public StackHandler(SlimefunItem original, DistinctiveItem distinctive) {
            this.original = original;
            this.distinctive = distinctive;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // Call origin
            if (original instanceof DistinctiveItem di) {
                if (di.canStack((ItemMeta) args[0], (ItemMeta) args[1])) {
                    return true;
                }
            }

            // Call applied
            if ("canStack".equals(method.getName())) {
                return distinctive.canStack((ItemMeta) args[0], (ItemMeta) args[1]);
            }

            return null;
        }
    }
}
