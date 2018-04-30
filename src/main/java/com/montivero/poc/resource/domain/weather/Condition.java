package com.montivero.poc.resource.domain.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.montivero.poc.resource.domain.persistence.LongIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Condition extends LongIdentifiable implements Comparable<Condition> {

    @Column(name = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected LocalDate date;
    @Column(name = "CONDITION_DESCRIPTION")
    protected String conditionDescription;

    public int compareTo(Condition otherCondition) {
        return this.getDate().compareTo(otherCondition.getDate());
    }

}
