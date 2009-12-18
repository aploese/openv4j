/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

import java.io.Serializable;

/**
 *
 * @author aploese
 */
public enum Protocol  implements  Serializable {
    GWG,
    KW,
    _300;

    public String getName() {
        return name();
    }

    public String getLabel() {
        return name();
    }

}
