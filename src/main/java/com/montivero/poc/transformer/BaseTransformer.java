package com.montivero.poc.transformer;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransformer<FROM, TO> {

    public List<TO> transformList(List<FROM> list) {
        List<TO> countries = new ArrayList<>();
        for (FROM t : CollectionUtils.emptyIfNull(list)) {
            countries.add(transform(t));
        }
        return countries;
    }

    public abstract TO transform(FROM t);

}
