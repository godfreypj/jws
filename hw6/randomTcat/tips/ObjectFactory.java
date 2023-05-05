
package tips;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the tips package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTipsResponse_QNAME = new QName("http://tips/", "getTipsResponse");
    private final static QName _GetTipResponse_QNAME = new QName("http://tips/", "getTipResponse");
    private final static QName _GetTip_QNAME = new QName("http://tips/", "getTip");
    private final static QName _GetTips_QNAME = new QName("http://tips/", "getTips");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: tips
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTips }
     * 
     */
    public GetTips createGetTips() {
        return new GetTips();
    }

    /**
     * Create an instance of {@link GetTip }
     * 
     */
    public GetTip createGetTip() {
        return new GetTip();
    }

    /**
     * Create an instance of {@link GetTipResponse }
     * 
     */
    public GetTipResponse createGetTipResponse() {
        return new GetTipResponse();
    }

    /**
     * Create an instance of {@link GetTipsResponse }
     * 
     */
    public GetTipsResponse createGetTipsResponse() {
        return new GetTipsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTipsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tips/", name = "getTipsResponse")
    public JAXBElement<GetTipsResponse> createGetTipsResponse(GetTipsResponse value) {
        return new JAXBElement<GetTipsResponse>(_GetTipsResponse_QNAME, GetTipsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTipResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tips/", name = "getTipResponse")
    public JAXBElement<GetTipResponse> createGetTipResponse(GetTipResponse value) {
        return new JAXBElement<GetTipResponse>(_GetTipResponse_QNAME, GetTipResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTip }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tips/", name = "getTip")
    public JAXBElement<GetTip> createGetTip(GetTip value) {
        return new JAXBElement<GetTip>(_GetTip_QNAME, GetTip.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTips }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tips/", name = "getTips")
    public JAXBElement<GetTips> createGetTips(GetTips value) {
        return new JAXBElement<GetTips>(_GetTips_QNAME, GetTips.class, null, value);
    }

}
