# DiaryGram

나만의 작고 소중한 일기장 DiaryGram을 통해 소중한 추억을 보낸 사람들을 태그해보세요!

## OutLine

DiaryGram은 당신의 소중한 추억들을 함께했던 사람들을 기록하고, 그 추억들이 담긴 사진을 올릴 수 있는 나만의 일기장 앱입니다.

연락처탭, 갤러리탭, 포스트탭 총 세 가지 탭으로 구성되어있습니다.

## Team

[송종찬(성균관대학교)](https://github.com/jongchan159)

[김문정(카이스트)](https://github.com/coco483)

## Details

### Intro

### Tab 1 : 연락처

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/3e090733-c2eb-4262-a8e0-9b39c62d1984/Untitled.png)

연락처를 목록으로 볼 수 있습니다.
연락처를 클릭하면 삭제 버튼과 상세 정보 버튼이 보이게 됩니다.

### 연락처 추가

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/eb832bb2-1289-4f1a-92d3-787ed57e0ac9/Untitled.png)

프로필 이미지, 이름, 전화번호를 입력하여 연락처를 추가합니다.
이미지는 카메라로 사진을 찍거나 핸드폰 갤러리에서 가져올 수 있습니다.
프로필 이미지가 없는 경우 기본 아이콘으로 대체됩니다.

### 검색 기능

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/50af4deb-8dc2-46df-af44-46063718e5d0/Untitled.png)

이름과 전화번호를 기준으로 검색할 수 있도록 검색 기능을 구현하였습니다.

### 연락처 상세페이지

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/3d5462fe-7778-4a7f-bf43-2e27fe501551/Untitled.png)

프로필 이미지, 이름, 연락처를 편집할 수 있습니다.
해당 연락처가 태그된 포스트들을 볼 수 있습니다.
포스트를 클릭하면 포스트 상세페이지로 넘어갑니다.

### Tab2 : 갤러리

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/0902f84b-c0dd-44d5-8ae2-bff09f723d83/Untitled.png)

갤러리에서는 연락처의 프로필과 포스트에 업로드된 사진들을 모두 볼 수 있습니다.
각 요소는 해당 사진이 포함된 연락처 또는 포스트와 연결되어 사진을 클릭하면 연락처 상세정보 창 혹은 포스트 상세정보 창으로 이동합니다.

### Tab 3 : 포스트

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/b0a246a6-1fc9-4cc3-a571-ae9870e98a6f/Untitled.png)

등록했던 포스트들을 목록으로 볼 수 있습니다.
포스트를 클릭하면 삭제와 상세정보 버튼이 보이게 됩니다.
이미지, 내용, 태그한 연락처들과 포스트를 등록한 시간을 볼 수 있습니다.

### 포스트 등록

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/03860f85-3440-404b-b160-385e3554ba91/Untitled.png)

### 태그 기능

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/06736950-c5c0-4c9c-aef7-81067cd28587/Untitled.png)

이름으로 연락처를 검색하여 연락처를 포스트에 태그할 수 있습니다.
AutoCompleteTextView를 통해 이름을 입력하면 연락처를 검색하고, 클릭 시 자동 완성되어 태그 목록에 올라가도록 구현하였습니다.
포스트 목록 혹은 상세페이지에서 해당 태그들을 클릭하면 연락처 상세정보로 넘어갑니다.

### 검색

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/14c824c0-f735-4fb3-9442-d2ab881e7f26/Untitled.png)

내용을 기준으로 검색할 수 있도록 검색 기능을 구현했습니다.

### 포스트 상세페이지

(img)

상세페이지에서 사진과 내용, 태그를 편집하는 기능을 구현하였습니다.


