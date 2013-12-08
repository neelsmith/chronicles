package edu.holycross.shot.chron


class Synchronism {


    String evidence
    String src
    String target

    String synchronization
    String synchtype

    Number units

    // default value
    Precision precision = Precision.TROPICAL_YEAR

    JGraph jGraph = null


    // depends on precision...
    Number weight() {
        switch (this.synchronization) {
            case "contemporary":
                return 0
            break
            case "precedes":
                return (this.units * -1)

            break
            case "follows":
                return this.units 
            default :
                return null
            break

        }
    }


    void initValues(ArrayList columns) {
        evidence = columns[0]
        src = columns[1]
        synchronization = columns[2]
        synchtype = columns[3]
        target = columns[4]
        if ((columns[5] != null) && (columns[5].isNumber())) {
            String numStr = columns[5]
            units = numStr.toBigDecimal()
        } else {
            units = null
        }

    }

    Synchronism(ArrayList columns, JGraph jg) {
        initValues(columns)
        this.jGraph = jg   
    }
    /** Constructor to initialize a Synchronism
    * from an ordered list of values.
    */
    Synchronism(ArrayList columns) {
        initValues(columns)
    }

    String getUnitString() {
        // depends on precision
        switch (precision) {
            case Precision.TROPICAL_YEAR:
                return " ${units} years"
            break
            default:
                return "${units} units (${precision }not yet supported)"
            break
        }
    }

    String toString() {
        StringBuffer buff = new StringBuffer()
        if (jGraph != null) {
            buff.append(jGraph.getLabel(this.src))
        } else {
            buff.append(this.src)
        }
        
        switch (synchronization) {
            case "precedes":
                if (jGraph != null) {
                buff.append(" precedes ${jGraph.getLabel(this.target)}")
            } else {
                buff.append(" precedes ${this.target}")
            }
            if (this.units != null) {
                buff.append(" by ${getUnitString()}")
            }
            break

            case "follows":
                if (jGraph != null) {
                buff.append(" follows ${jGraph.getLabel(this.target)}")
            } else {
                buff.append(" follows ${this.target}")
            }
            if (this.units != null) {
                buff.append(" by ${getUnitString()}")
            }

            break

            case "contemporary":
                if (jGraph != null) {
                buff.append(" contemporary with ${jGraph.getLabel(this.target)}")
            } else {
                buff.append(" contemporary with ${this.target}")
            }
            break
        }
    }
}
