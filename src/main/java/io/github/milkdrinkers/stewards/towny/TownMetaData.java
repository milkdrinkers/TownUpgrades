package io.github.milkdrinkers.stewards.towny;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import io.github.milkdrinkers.stewards.utility.Cfg;

public class TownMetaData {

    private static final String bankLimit = "stewards_bank_limit";

    private static final IntegerDataField bankLimitField = new IntegerDataField(bankLimit);

    public static int getBankLimit(Town town) {
        if (!MetaDataUtil.hasMeta(town, bankLimit))
            MetaDataUtil.addNewIntegerMeta(town, bankLimit, Cfg.get().getInt("treasurer.limit.level-0"), true); // TODO Get level of banker and set limit
        return MetaDataUtil.getInt(town, bankLimitField);
    }

    public static void setBankLimit(Town town, int limit) {
        if (!MetaDataUtil.hasMeta(town, bankLimit)) {
            MetaDataUtil.addNewIntegerMeta(town, bankLimit, Cfg.get().getInt("treasurer.limit.level-0"), true);
            return;
        }
        MetaDataUtil.setInt(town, bankLimitField, limit, true);
    }

}
