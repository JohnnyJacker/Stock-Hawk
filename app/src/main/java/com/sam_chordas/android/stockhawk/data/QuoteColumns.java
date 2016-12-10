package com.sam_chordas.android.stockhawk.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.Constraints;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;
import net.simonvt.schematic.annotation.UniqueConstraint;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;

/**
 * Created by sam_chordas on 10/5/15.
 */

@Constraints(
        unique = @UniqueConstraint(
                name = "UNQ_TAG_FOR_SYMBOL",
                columns = {QuoteColumns.SYMBOL},
                onConflict = REPLACE)
)


public class QuoteColumns {
  @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
  public static final String _ID = "_id";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String SYMBOL = "symbol";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String PERCENT_CHANGE = "percent_change";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String CHANGE = "change";
  @DataType(DataType.Type.TEXT) @NotNull
  public static final String BIDPRICE = "bid_price";
  @DataType(DataType.Type.TEXT)
  public static final String CREATED = "created";
  @DataType(DataType.Type.INTEGER) @NotNull
  public static final String ISUP = "is_up";
  @DataType(DataType.Type.INTEGER) @NotNull
  public static final String ISCURRENT = "is_current";
}
