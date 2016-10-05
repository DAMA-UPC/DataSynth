package org.dama.datasynth.schnappi.ast;

import org.dama.datasynth.common.Types;

/**
 * Created by aprat on 24/08/16.
 * Represents a numerical lieral in the Schnappi Ast.
 */
public class Number extends Literal {

    private Types.DataType dataType = null;

    /**
     * Constructor
     * @param value The numeric value of the literal
     * @param type The numeric type of the literal
     */
    public Number(String value, Types.DataType type) {
        super(value);
        this.dataType = type;
    }

    /**
     * Copy constructor
     * @param number The number to copy from
     */
    public Number(Number number) {
        super(number);
        this.dataType = number.dataType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Gets the Object equivalent of the literal
     * @return The object equivalent of the literal
     */
    @Override
    public Object getObject() {
        if(dataType == Types.DataType.LONG) {
            return Long.parseLong(getValue());
        } else if(dataType == Types.DataType.DOUBLE) {
            return Double.parseDouble(getValue());
        }
        return null;
    }

    /**
     * Gets the data type of the number
     * @return The data type of the number
     */
    public Types.DataType getDataType() {
        return dataType;
    }

    @Override
    public Number clone() {
        return new Number(this);
    }

    @Override
    public java.lang.String toString() {
        return "<"+dataType.getText()+","+value+">";
    }
}
