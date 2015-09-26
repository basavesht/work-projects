
package com.tcs.bancs.services.provider.payments.transfers.interfaces.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "selectAccount", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectAccount", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers")
public class SelectAccount {

    @XmlElement(name = "SelectAccountRequestParam", namespace = "http://tcs.com/bancs/morganstanley/payments/transfers/selectAccount/request")
    private com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest SelectAccountRequestParam;

    /**
     * 
     * @return
     *     returns TransferRequest
     */
    public com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest getSelectAccountRequestParam() {
        return this.SelectAccountRequestParam;
    }

    /**
     * 
     * @param SelectAccountRequestParam
     *     the value for the SelectAccountRequestParam property
     */
    public void setSelectAccountRequestParam(com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest SelectAccountRequestParam) {
        this.SelectAccountRequestParam = SelectAccountRequestParam;
    }

}
