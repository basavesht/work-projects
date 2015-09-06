
package com.tcs.bancs.services.provider.payments.transfers.interfaces.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "selectAccountResponse", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectAccountResponse", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
public class SelectAccountResponse {

    @XmlElement(name = "SelectAccountResponseParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/response")
    private com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse _return;

    /**
     * 
     * @return
     *     returns TransferResponse
     */
    public com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse _return) {
        this._return = _return;
    }

}
