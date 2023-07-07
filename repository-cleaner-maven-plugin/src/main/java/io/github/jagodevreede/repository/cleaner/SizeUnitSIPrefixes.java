package io.github.jagodevreede.repository.cleaner;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SizeUnitSIPrefixes {
    Bytes(1L),
    KB(Bytes.unitBase * 1000),
    MB(KB.unitBase * 1000),
    GB(MB.unitBase * 1000),
    TB(GB.unitBase * 1000),
    PB(TB.unitBase * 1000),
    EB(PB.unitBase * 1000);

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

    private final Long unitBase;

    private static List<SizeUnitSIPrefixes> unitsInDescending() {
        List<SizeUnitSIPrefixes> list = Arrays.asList(values());
        Collections.reverse(list);
        return list;
    }

    public static String toHumanReadable(long size) {
        List<SizeUnitSIPrefixes> units = SizeUnitSIPrefixes.unitsInDescending();
        if (size < 0) {
            throw new IllegalArgumentException("Invalid file size: " + size);
        }
        String result = null;
        for (SizeUnitSIPrefixes unit : units) {
            if (size >= unit.getUnitBase()) {
                result = formatSize(size, unit.getUnitBase(), unit.name());
                break;
            }
        }
        return result == null ? formatSize(size, SizeUnitSIPrefixes.Bytes.getUnitBase(), SizeUnitSIPrefixes.Bytes.name()) : result;
    }

    private static String formatSize(long size, long divider, String unitName) {
        return DEC_FORMAT.format((double) size / divider) + " " + unitName;
    }

    SizeUnitSIPrefixes(Long unitBase) {
        this.unitBase = unitBase;
    }

    public Long getUnitBase() {
        return unitBase;
    }
}
