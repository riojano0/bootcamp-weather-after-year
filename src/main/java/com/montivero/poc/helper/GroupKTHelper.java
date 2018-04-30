package com.montivero.poc.helper;

import com.montivero.poc.client.domain.groupKT.GroupKTResponse;
import com.montivero.poc.client.domain.groupKT.GroupKTRestResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupKTHelper {

    public static <T> GroupKTRestResponse<T> getRestResponse(GroupKTResponse<T> groupKTResponse) {
        return groupKTResponse != null
                ? groupKTResponse.getRestResponse()
                : null;
    }

    public static <T> T getResult(GroupKTRestResponse<T> groupKTRestResponse) {
        return (groupKTRestResponse != null)
                ? groupKTRestResponse.getResult()
                : null;
    }

}
