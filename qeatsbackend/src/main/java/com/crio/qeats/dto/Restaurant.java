
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

// TODO: CRIO_TASK_MODULE_SERIALIZATION
//  Implement Restaurant class.
// Complete the class such that it produces the following JSON during serialization.
// {
//  "restaurantId": "10",
//  "name": "A2B",
//  "city": "Hsr Layout",
//  "imageUrl": "www.google.com",
//  "latitude": 20.027,
//  "longitude": 30.0,
//  "opensAt": "18:00",
//  "closesAt": "23:00",
//  "attributes": [
//    "Tamil",
//    "South Indian"
//  ]
// }

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant 
{
    //Serialization -> We are creating a JSON here.
    @JsonIgnore
    @NonNull
    private String id;

    @NonNull
    private String restaurantId;

    @NonNull
    private String name;

    @NonNull
    private String city;

    @NonNull
    private String imageUrl;

    @NonNull
    private Double latitude;

    @NonNull
    private Double longitude;

    @NonNull
    private String opensAt;

    @NonNull
    private String closesAt;

    @NonNull
    private List<String> attributes;


    public String getOpensAt()
    {
        return opensAt;
    }
    public String getClosesAt()
    {
        return closesAt;
    }

    public void setOpensAt(String opensAt)
    {
        this.opensAt = opensAt;
    }
    public void setClosesAt(String closesAt)
    {
        this.closesAt = closesAt;
    }
}

