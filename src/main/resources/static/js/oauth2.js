window.addEventListener('DOMContentLoaded', async()=>{
        await fetchByPost("/users/oauth/login/kakao"+window.location.search,
            convertQueryStringToJson(),
            (response) => {
                console.log(response);
                // window.close();
            },
            () => {},
        );
    }
)