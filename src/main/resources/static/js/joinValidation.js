$(function(){
    let checkid=false;	 //아이디의 정규식 체크하기 위한 변수로 기본값은 false, 규칙에 맞게 입력하면 true값을 갖습니다.
    let checkemail=false;//이메일의 정규식 체크하기 위한 변수로 기본값은 false, 규칙에 맞게 입력하면 true값을 갖습니다.

    $("input[name=user_id]").on('keyup',function(){

        //[A-Za-z0-9_]의 의미 => \w
        const pattern = /^\w{2,30}$/;
        const id = $(this).val();

        if(!pattern.test(id)){
            $("#id-message").css('color','red')
                .html("영문이나 숫자를 이용한 2 ~ 30 글자의 아이디를 사용해 주세요.");
            checkid = false;
            return;
        }

        $.ajax({  //아이디 중복검사 : 개인회원
            url	: "idcheck",
            data : {"id" : id},
            success	: function(resp){
                if(resp == "-1"){//db에 해당 id가 없는 경우
                    $("#id-message").css('color','green').html("사용 가능한 아이디 입니다.");
                    checkid=true;
                }else{//db에 해당 id가 있는 경우(1)
                    $("#id-message").css('color','blue').html("사용중인 아이디 입니다.");
                    checkid=false;
                }
            }
        });//아이디중복검사 : 개인회원 ajax 끝

    })//id keyup 끝

    $("#regular_email").on('keyup',function(){ //개인회원의 이메일 형식, 고유성 검사
        //[A-Za-z0-9_]와 동일한 것이 \w입니다.
        //+는 1회 이상 반복을 의미하고 {1,}와 동일합니다.
        //\w+ 는 [A-Za-z0-9_]를 1개이상 사용하라는 의미입니다.
        const pattern = /^\w+@\w+[.][A-Za-z0-9]{3}$/;
        const email = $(this).val();

        if(!pattern.test(email)){
            $("#email-message").css('color','red')
                .html("올바른 이메일을 작성해 주세요.");
            checkemail=false;
        }else { //이메일 형식이 올바를 시, 이메일 중복체크
            $("#email-message").css('color', 'green')
                .html("이메일이 형식에 맞습니다.");
            checkemail = true;

            $.ajax({  //이메일 중복검사 : 개인회원
                url: "emailcheck",
                data: {"email": email},
                success: function (resp) {
                    if (resp == "-1") {//db에 해당 id가 없는 경우
                        $("#email-message").css('color', 'green').html("사용 가능한 이메일 입니다.");
                        checkemail = true;
                    } else {//db에 해당 id가 있는 경우(1)
                        $("#email-message").css('color', 'blue').html("사용중인 이메일 입니다.");
                        checkemail = false;
                    }
                }
            });//이메일 중복검사 ajax 끝

        }
    })//개인회원 email keyup 이벤트 처리 끝


    //개인회원가입 버튼 제출시 유효성 검사
    $('form[name="regularJoinProcess"]').off('submit').submit(function(){
        const telPattern = /^[0-9]{8,13}$/;
        if (!telPattern.test($("input[name='tel']").val())) {
            alert("전화번호를 확인해 주세요.");
            $("input[name=tel]").val('').focus();
            return false;
        }

        const juminPattern = /^[0-9]{2}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}$/;
        if (!juminPattern.test($("input[name='jumin']").val())) {
            alert("주민등록번호를 확인해 주세요.");
            $("input[name='jumin']").val('').focus();
            return false;
        }


        if(!$.isNumeric($("input[name='jumin']").val())){
            alert("주민등록번호에는 숫자를 입력해 주세요.");
            $("input[name='jumin']").val('').focus();
            return false;
        }

        if(!$.isNumeric($("input[name='gender']").val())){
            alert("주민번호 뒷자리에는 숫자를 입력해 주세요");
            $("input[name='gender']").val('').focus();
            return false;
        }

        if (!["1", "2", "3", "4"].includes($("input[name='gender']").val())) {
            alert("주민번호 뒷자리는 1, 2, 3, 4 중 하나여야 합니다");
            $("input[name='gender']").val('').focus();
            return false;
        }

        if(!$.isNumeric($("input[name='tel']").val())){
            alert("전화번호는 숫자로 입력해 주세요");
            $("input[name='tel']").val('').focus();
            return false;
        }

        if ($("input[name='tel']").val().length !== 11) {
            alert("전화번호가 11자리가 맞는지 확인해 주세요.");
            $("input[name='tel']").val('').focus();
            return false;
        }

        if(!checkid){
            alert("사용 가능한 id를 입력해 주세요.");
            $("input[name=user_id]").val('').focus();
            $("#id-message").text('');
            return false;
        }

        if(!checkemail){
            alert("이메일 형식을 확인해 주세요.");
            $("input[name=email]").focus();
            return false;
        }
    })// 개인회원가입 submit 끝

})//ready