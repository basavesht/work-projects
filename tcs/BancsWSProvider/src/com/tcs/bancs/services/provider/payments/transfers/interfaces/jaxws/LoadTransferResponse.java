
package com.tcs.bancs.services.provider.payments.transfers.interfaces.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "loadTransferResponse", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadTransferResponse", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
public class LoadTransferResponse {

    @XmlElement(name = "LoadTransferResponseParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/response")
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
