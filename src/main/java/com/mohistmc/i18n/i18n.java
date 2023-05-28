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

import lombok.Getter;
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
    public static Map<String, String> CURRENT_CACHE = new ConcurrentHashMap<>();
    private static PropertyResourceBundle rb;
    private static final String properties = "message";
    @Getter
    private static Locale locale;
    private static InputStream inputStream;

    @SneakyThrows
    public i18n(ClassLoader classLoader, Locale locale) {
        i18n.locale = locale;
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
    }

    @SneakyThrows
    public i18n(Class<?> classz, Locale locale) {
        i18n.locale = locale;
        String lang = "_" + locale.getLanguage() + "_" + locale.getCountry();
        InputStream deFinputStream = classz.getResourceAsStream("/lang/" + properties + ".properties");
        inputStream = classz.getResourceAsStream("/lang/" + properties + lang + ".properties");
        if (inputStream == null) {
            inputStream = deFinputStream;
        } else if (inputStream == null) {
            System.out.println("invalid language file");
            System.exit(0);
        }
        rb = new PropertyResourceBundle(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }


    public String get(String key) {
        String string = rb.getString(key);
        if (!CURRENT_CACHE.containsKey(key)) {
            CURRENT_CACHE.put(key, string);
        } else {
            return CURRENT_CACHE.get(key);
        }
        return string;
    }

    public String get(String key, Object... f) {
        return new MessageFormat(get(key)).format(f);
    }

    public boolean isCN() {
        TimeZone timeZone = TimeZone.getDefault();
        return "Asia/Shanghai".equals(timeZone.getID()) || "CN".equals(locale.getCountry());
    }
}
