package com.example.covidtrackerapp

import retrofit2.http.GET

interface CasesByPlaceApi {

    @GET("/echo?user_content_key=KuTVJF0VBcj9NHLm8Scj_zl2zkeJsUe6PcsjEAKjHoPQsHHONCQihxEMLW24" +
            "Fx1GIEhG3GjoaH3Dxq_MgLn2xHBOC5sD988dOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5" +
            "hHa4rb-T7Ov7g7X2sWhXRW-yAjzAP5umqJGVQQzPc9y5OCx7KJpjB9n6AH086q5WBPtq_OIHkwB74vJIKBH7" +
            "cAYd0itYMDtKAUnqIS2XtWCc3tbnJqmnH9VlYQ9L2jjwk5KkqtDGHkMqdWvFGBwa0WUZvfoxUazFlBWladS" +
            "yjz5NdT&lib=Mhljf9zbpRDwJCP_QfypDOubVB-MXoX5v")
    fun getPlace ():retrofit2.Call<AreaObject>


}