/*
 * Copyright 2014 astamuse company,Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.asta.snippet.form;

import static com.astamuse.asta4d.render.SpecialRenderer.Clear;

import java.util.Arrays;
import java.util.List;

import com.astamuse.asta4d.render.Renderer;
import com.asta.handler.form.SubPersonForm;
import com.asta.util.persondb.Person;
import com.asta.util.persondb.PersonDbManager;
import com.astamuse.asta4d.util.SelectorUtil;
import com.astamuse.asta4d.util.collection.RowRenderer;
import com.astamuse.asta4d.web.annotation.QueryParam;

public class ListSnippet {

    public Renderer render() {
        List<Person> personList = PersonDbManager.instance().findAll();
        return Renderer.create(".x-row", personList, new RowRenderer<Person>() {
            @Override
            public Renderer convert(int rowIndex, Person row) {
                Renderer renderer = Renderer.create();
                renderer.add(".x-check input", "value", row.getId());
                renderer.add(".x-name", row.getName());
                renderer.add(".x-age", row.getAge());
                if (row instanceof SubPersonForm) {
                    renderer.add(".x-sex", "-");
                    renderer.add(".x-blood", "-");
                    renderer.add(".x-language", "-");
                } else {
                    renderer.add(".x-sex", row.getSex());
                    renderer.add(".x-blood", row.getBloodType());
                    renderer.add(".x-language span", Arrays.asList(row.getLanguage()));
                }
                return renderer;
            }
        });
    }

    public Renderer showTabs(@QueryParam String type) {
        Renderer renderer = Renderer.create();
        renderer.add("li", "-class", "active");

        String targetLi = SelectorUtil.id("li", type);
        renderer.add(targetLi, "+class", "active");
        renderer.add(targetLi + " a", "href", Clear);

        String targetBtn = SelectorUtil.id("div", type);
        renderer.add(targetBtn, "-class", "x-remove");
        renderer.add(".x-remove", Clear);

        return renderer;
    }
}
