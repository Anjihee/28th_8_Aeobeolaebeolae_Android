package com.surround2023.surround2023.set_location

data class ResultSearchKeyword(
    var documents: List<Result>          // 검색 결과
)

data class PlaceMeta(
    var total_count: Int, // 검색어에 검색된 문서 수
    var pageable_count: Int, // total_count 중 노출 가능 문서 수, 최대 45 (API에서 최대 45개 정보만 제공)
    var is_end: Boolean, // 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    var same_name: RegionInfo // 질의어의 지역 및 키워드 분석 정보
)

data class RegionInfo(
    var region: List<String>, // 질의어에서 인식된 지역의 리스트, ex) '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트
    var selected_region: String // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
)

data class Result(
    var address: Address
)

data class Address(
    var address_name: String,   // 전체 지번 주소
    val region_3depth_h_name:String,    //지역 3 Depth,  행정동 명칭
    var region_3depth_name:String   //지역 3 Depth, 동 단위
)
