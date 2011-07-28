<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://10.15.1.80:8443/sftest/" xmlns:s0="urn:authentication.soap.sforce.com" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" targetNamespace="http://10.15.1.80:8443/sftest/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="urn:authentication.soap.sforce.com">
      <s:element name="Authenticate">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="username" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="password" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="sourceIp" type="s:string" />
            <s:any minOccurs="0" maxOccurs="unbounded" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="AuthenticateResult">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="Authenticated" type="s:boolean" />
          </s:sequence>
        </s:complexType>
      </s:element>
    </s:schema>
  </wsdl:types>
  <wsdl:message name="AuthenticateSoapIn">
    <wsdl:part name="parameters" element="s0:Authenticate" />
  </wsdl:message>
  <wsdl:message name="AuthenticateSoapOut">
    <wsdl:part name="parameters" element="s0:AuthenticateResult" />
  </wsdl:message>
  <wsdl:portType name="SimpleAdAuthSoap">
    <wsdl:operation name="Authenticate">
      <wsdl:input message="tns:AuthenticateSoapIn" />
      <wsdl:output message="tns:AuthenticateSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="SimpleAdAuthSoap" type="tns:SimpleAdAuthSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="Authenticate">
      <soap:operation soapAction="" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="SimpleAdAuthSoap12" type="tns:SimpleAdAuthSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="Authenticate">
      <soap12:operation soapAction="" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="SimpleAdAuth">
    <wsdl:port name="SimpleAdAuthSoap" binding="tns:SimpleAdAuthSoap">
      <soap:address location="https://10.15.1.80:8443/sftest/simple.asmx" />
    </wsdl:port>
    <wsdl:port name="SimpleAdAuthSoap12" binding="tns:SimpleAdAuthSoap12">
      <soap12:address location="https://10.15.1.80:8443/sftest/simple.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>