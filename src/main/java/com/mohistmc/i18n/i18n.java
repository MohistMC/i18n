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
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class i18n {
    public Map<String, String> CURRENT_CACHE = new ConcurrentHashMap<>();
    private PropertyResourceBundle rb;
    private PropertyResourceBundle defaultBundle;
    private final String properties = "message";
    private final Locale locale;

    @SneakyThrows
    public i18n(ClassLoader classLoader, Locale locale) {
        this.locale = locale(locale.toString());
        String lang = "_" + this.locale.getLanguage() + "_" + this.locale.getCountry();
        InputStream deFinputStream = classLoader.getResourceAsStream("lang/" + properties + ".properties");
        InputStream inputStream = classLoader.getResourceAsStream("lang/" + properties + lang + ".properties");
        if (inputStream == null) {
            inputStream = deFinputStream;
        } else if (inputStream == null) {
            System.out.println("[i18N] Invalid language file");
            System.exit(0);
        }
        prb(deFinputStream, inputStream);
    }

    public i18n(ClassLoader classLoader, String langByString) {
        this(classLoader, locale(langByString));
    }

    @SneakyThrows
    public i18n(Class<?> classz, Locale locale) {
        this.locale = locale(locale.toString());
        String lang = "_" + this.locale.getLanguage() + "_" + this.locale.getCountry();
        InputStream deFinputStream = classz.getResourceAsStream("/lang/" + properties + ".properties");
        InputStream inputStream = classz.getResourceAsStream("/lang/" + properties + lang + ".properties");
        if (inputStream == null) {
            inputStream = deFinputStream;
        } else if (inputStream == null) {
            System.out.println("[i18N] Invalid language file");
            System.exit(0);
        }
        prb(deFinputStream, inputStream);
    }

    public i18n(Class<?> classz, String langByString) {
        this(classz, locale(langByString));
    }

    private static Locale locale(String locale) {
        Locale defaultLocale = Locale.getDefault();
        if (locale.contains("_") && locale.split("_").length == 2) {
            String l = locale.split("_")[0];
            String c = locale.split("_")[1];
            defaultLocale = new Locale(l, c);
        }
        if (defaultLocale.getLanguage().isEmpty() || defaultLocale.getCountry().isEmpty()) {
            defaultLocale = new Locale("xx", "XX");
        }

        return defaultLocale;
    }

    @SneakyThrows
    private void prb(InputStream deFinputStream, InputStream in) {
        defaultBundle = new PropertyResourceBundle(new InputStreamReader(deFinputStream, StandardCharsets.UTF_8));
        rb = new PropertyResourceBundle(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    @Deprecated
    public String as(String key) {
        String string;
        if (rb.containsKey(key)) {
            string = rb.getString(key);
        } else {
            if (defaultBundle.containsKey(key)) {
                string = defaultBundle.getString(key);
            } else {
                string = key;
            }
        }
        if (!CURRENT_CACHE.containsKey(key)) {
            CURRENT_CACHE.put(key, string);
        } else {
            return CURRENT_CACHE.get(key);
        }
        return string;
    }

    /**
     *  Use %s instead of {0-9}
     * @param key
     * @param f
     * @return
     */
    public String as(String key, Object... f) {
        return as(key).formatted(f);
    }

    public boolean isCN() {
        return is("CN");
    }

    public boolean is(String country) {
        return Locale.getDefault().getCountry().equals(country) || country.equals(locale.getCountry());
    }
}
