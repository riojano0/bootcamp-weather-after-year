package com.montivero.poc.transformer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransformer<FROM, TO> implements Transformer<FROM, TO> {

    public List<TO> transformList(List<FROM> list) {
        List<TO> toList = new ArrayList<>();
        for (FROM t : CollectionUtils.emptyIfNull(list)) {
            toList.add(transform(t));
        }
        return toList;
    }

    public abstract TO transform(FROM t);

}
