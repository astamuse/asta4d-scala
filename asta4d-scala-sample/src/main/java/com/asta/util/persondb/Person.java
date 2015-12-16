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
package com.asta.util.persondb;

import java.lang.reflect.InvocationTargetException;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.asta.handler.form.PersonForm;
import com.astamuse.asta4d.util.collection.RowConvertor;
import com.astamuse.asta4d.web.form.field.OptionValueMap;
import com.astamuse.asta4d.web.form.field.OptionValuePair;

public class Person implements IdentifiableEntity, Cloneable {

    public static enum BloodType {
        A, B, O, AB;

        public static final OptionValueMap asOptionValueMap = OptionValueMap.build(BloodType.values(),
                new RowConvertor<BloodType, OptionValuePair>() {
                    @Override
                    public OptionValuePair convert(int rowIndex, BloodType obj) {
                        return new OptionValuePair(obj.name(), obj.name());
                    }
                });
    }

    public static enum SEX {
        Male, Female;

        public static final OptionValueMap asOptionValueMap = OptionValueMap.build(SEX.values(), new RowConvertor<SEX, OptionValuePair>() {
            @Override
            public OptionValuePair convert(int rowIndex, SEX obj) {
                return new OptionValuePair(obj.name(), obj.name());
            }
        });
    }

    public static enum Language {
        English, Japanese, Chinese;

        public static final OptionValueMap asOptionValueMap = OptionValueMap.build(Language.values(),
                new RowConvertor<Language, OptionValuePair>() {
                    @Override
                    public OptionValuePair convert(int rowIndex, Language obj) {
                        return new OptionValuePair(obj.name(), obj.name());
                    }
                });
    }

    private String name;

    private Integer age;

    private BloodType bloodType = BloodType.AB;

    private SEX sex;

    private Language[] language;

    private String memo;

    // @ShowCode:showValidationAnnotationStart
    /*
     * bean validation is annotated on entity POJO
     * 
     * (it can also be done at the form POJO side, but we think the value 
     * constrains should be considered as a part of the entity POJO's feature)
     */

    @NotBlank
    @Size(max = 6)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Max(45)
    @NotNull
    public Integer getAge() {
        return age;
    }

    // @ShowCode:showValidationAnnotationEnd

    public void setAge(Integer age) {
        this.age = age;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public SEX getSex() {
        return sex;
    }

    public void setSex(SEX sex) {
        this.sex = sex;
    }

    public Language[] getLanguage() {
        return language;
    }

    public void setLanguage(Language[] language) {
        this.language = language;
    }

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static Person createByForm(PersonForm form) {
        Person p = new Person();
        try {
            BeanUtils.copyProperties(p, form);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return p;
    }
}
