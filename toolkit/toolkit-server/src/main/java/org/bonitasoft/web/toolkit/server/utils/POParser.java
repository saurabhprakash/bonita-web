/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bonitasoft.web.toolkit.server.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;
import org.fedorahosted.tennera.jgettext.catalog.parse.ParseException;

/**
 * 
 * @author Séverin Moussel
 * 
 */
public class POParser {

    private final Map<String, String> translations = new TreeMap<String, String>();

    public POParser() {
    }

    public static Map<String, String> parse(final String path) {
        return new POParser().parseFile(path);
    }

    public static Map<String, String> parse(final File file) {
        return new POParser().parseFile(file);
    }

    public Map<String, String> parseFile(final String path) {
        return this.parseFile(new File(path));
    }

    public Map<String, String> parseFile(final File file) {
        try {
            parseCatalog(new PoParser().parseCatalog(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("I18N: File not found " + file.getPath());
        } catch (ParseException e) {
            throw new RuntimeException("I18N: Couldnt parse po file " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("I18N: IO issues while parsing " + file.getPath());
        }

        return this.translations;
    }

    private void parseCatalog(Catalog catalog) {
        Iterator<Message> it = catalog.iterator();
        while (it.hasNext()) {
            Message msg = it.next();
            translations.put(msg.getMsgid(), msg.getMsgstr());
        }
    }
}
