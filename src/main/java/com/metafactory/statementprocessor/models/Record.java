package com.metafactory.statementprocessor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "reference", "accountNumber", "description", "startBalance", "mutation", "endBalance" })
public class Record {
    @JacksonXmlProperty(isAttribute = true)
    private String reference;
    private String accountNumber;
    private String description;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private BigDecimal endBalance;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Record other = (Record) obj;

        return reference.equals(other.reference);
    }

    @JsonIgnore
    public boolean isValid() {
        if (startBalance == null || mutation == null || endBalance == null) {
            return false;
        }
        return startBalance.add(mutation).equals(endBalance);
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
