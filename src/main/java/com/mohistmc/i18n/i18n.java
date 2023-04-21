/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.i18n;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class i18n {
    public static Map<ClassLoader, Map<String, String>> CACHE = new ConcurrentHashMap<>();
    public static Map<String, String> CURRENT_CACHE = new ConcurrentHashMap<>();
    private static PropertyResourceBundle rb;
    private static ClassLoader classLoader;
    private static String properties = "message";
    private static Locale locale;
    private static InputStream inputStream;

    /**
     * Store as a Map with ClassLoader as the key  -> CACHE ↑
     */
    @Deprecated
    private boolean init = false;

    @SneakyThrows
    public void build(ClassLoader classLoader, Locale locale) {
        if (init) {
            System.out.println("Mohist i18n For use by Mohist only");
            System.exit(0);
        }
        this.classLoader = classLoader;
        this.locale = locale;
        String lang = "_" + locale.getLanguage() + "_" + locale.getCountry();
        InputStream deFinputStream = classLoader.getResourceAsStream("lang/" + properties + ".properties");
        inputStream = classLoader.getResourceAsStream("lang/" + properties + lang + ".properties");
        if (inputStream == null) {
            inputStream = deFinputStream;
        } else if (inputStream == null) {
            System.out.println("invalid language file");
            System.exit(0);
        }
        rb = new PropertyResourceBundle(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        init = true;
    }


    public static String get(String key) {
        String string = rb.getString(key);
        if (!CURRENT_CACHE.containsKey(key)) {
            CURRENT_CACHE.put(key, string);
        } else {
            return CURRENT_CACHE.get(key);
        }
        return string;
    }

    public static String get(String key, Object... f) {
        return new MessageFormat(get(key)).format(f);
    }

    public static boolean isCN() {
        TimeZone timeZone = TimeZone.getDefault();
        return "Asia/Shanghai".equals(timeZone.getID()) || "CN".equals(rb.getLocale().getCountry());
    }
}