
package com.tcs.bancs.services.provider.payments.transfers.interfaces.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createTransfer", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createTransfer", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
public class CreateTransfer {

    @XmlElement(name = "CreateTransferRequestParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/createTransfer/request")
    private com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest CreateTransferRequestParam;

    /**
     * 
     * @return
     *     returns TransferRequest
     */
    public com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest getCreateTransferRequestParam() {
        return this.CreateTransferRequestParam;
    }

    /**
     * 
     * @param CreateTransferRequestParam
     *     the value for the CreateTransferRequestParam property
     */
    public void setCreateTransferRequestParam(com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest CreateTransferRequestParam) {
        this.CreateTransferRequestParam = CreateTransferRequestParam;
    }

}
