
package com.tcs.bancs.services.provider.payments.transfers.interfaces.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "loadTransfer", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadTransfer", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
public class LoadTransfer {

    @XmlElement(name = "loadTransferRequestParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/loadTransfer/request")
    private com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest loadTransferRequestParam;

    /**
     * 
     * @return
     *     returns TransferRequest
     */
    public com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest getLoadTransferRequestParam() {
        return this.loadTransferRequestParam;
    }

    /**
     * 
     * @param loadTransferRequestParam
     *     the value for the loadTransferRequestParam property
     */
    public void setLoadTransferRequestParam(com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest loadTransferRequestParam) {
        this.loadTransferRequestParam = loadTransferRequestParam;
    }

}
