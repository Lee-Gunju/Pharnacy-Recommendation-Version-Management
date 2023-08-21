package com.example.project.api.service

import com.example.project.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class KaKaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KaKaoAddressSearchService kaKaoAddressSearchService

    def "address 파라미터 값이 null이면, requestAddressSearch 매소드는 null을 리턴한다."() {
        given:
        String address = null

        when:
        def result = kaKaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 vaild 하다면, requestAddressSearch 메소도는 정상적으로 document를 반환한다."() {
        given:
        def address = "서울 성북구 종암로 10길"

        when:
        def result = kaKaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList.get(0).addressName != null

    }

    def "정상적인 주소를 입력했을 경우, 정상적으로 위도 경도로 변환 된다."() {

        given:
        boolean actualresult = false

        when:
        def searchResult = kaKaoAddressSearchService.requestAddressSearch(inputAddress)

        then:
        if(searchResult == null) actualresult = false
        else actualresult = searchResult.getDocumentList().size() > 0


        where:
        inputAddress                            | expectedResult
        "서울 특별시 성북구 종암동"                   | true
        "서울 성북구 종암동 91"                     | true
        "서울 대학로"                             | true
        "서울 성북구 종암동 잘못된 주소"               | false
        "광진구 구의동 251-45"                     | true
        "광진구 구의동 251-455555"                 | false
        ""                                      | false

    }
}
