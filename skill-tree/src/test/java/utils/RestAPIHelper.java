package utils;

import java.util.HashMap;
import java.util.Map;

public class RestAPIHelper {
    public static Map<String, String> getSuperUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJzdXBlcl91c2VyIiwiaWF0IjoxNzEyMzk4MjM3LCJleHAiOjIxNDc0ODM2NDd9.GgzY90l1UB7rOzvHq5TPAV6Ujk5_uZdFY1o32odnZR1ATgihCOWhB2p5OmprJW5MbtGYQQTti1Vs7-ND5l8d7niIUb-xbDmQoQdLe5WQ18tXUaO-48D5aboh4FKk4J3OYvCtPcklgio43r9iYT40l89cMmdE-nzs2eO7dTwrC7i9l4H2fx1mzNAbBm8dKWwoRG4RxrZ8EKvIME5I0G6mPUwgpPuZae7YAOSs8Mzk4uVfN2jJqw8bbR5tWoTP-BPZ1z4MiI6hTnG5oOfQVe3ixXU7qrShNggHZ3GKNkNAxYezVWVM3jREXIc72gMaiiY3QKZ96K6QnUNLg-wgQ1fPRDWzo2gYF2UALI0SMINWwZ_hLHK922Jn9iVSCoGyx1dDFq6DUQ7zwNo9kO25YICRIrJuvX2Mat0VmUPeCN4sAQ9vElkFnyIDZ97B2s8rvE8LHGVShUbtcGY7sjlBAhVIWjz0bRTdb5LDcV9OermjjVXTH07H-GGdDGRw-tNQVzr0E7vvBEia4_5Zo6oyD5e_cM8Brlm5Nz_2SR-u8kqEGpkHtwuYTMElCDBUGMs5OvPs7m0_yCLL1f2Ktb9YoAQul-lNDDUAXPMRV29Ia5gXzvClbBq99aWJ7jI8OZWWdKciwBCgc71v6gfnOtu_VOXFGoF1hc08Pb8i_eUTLPPZ0bg");
        return cookie;
    }

    public static Map<String, String> getMemberCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJtZW1iZXIiLCJpYXQiOjE3MTIzOTgyMzcsImV4cCI6MjE0NzQ4MzY0N30.LcgW4b_Lzvn5Q383hACX4RTXfPoEMFnvqIEugfbIhINO38kDHGwgE9DTqu2fIXjczQhUX2lTrsC31rJ3Up_Vv9_yXNOD7NZ0JDThexYpAMlNUcFck-z6E3RjXYeiD-TCRzDLuJXKce_tMEqQ0HyvAD2Y4weIh-Non5lQcz_2BJeVxXpywv3duJdOvI9jVo_DvZIDsIDKxwwJbdDTghcHc_2KhYD3vMiX6I4cn6Pe7IuFyzNWe86m_QlI7FYcKHsKIB1y16XCVbzEwsv0zSmrRfjNbssq6Gz0ZKJn1Xl6eNWjn6lzFl3XPTGQH6TYM3VjhAQ5TB-e_mvYEWvUp6BirFgzCTwz3mLmk_8aP1b9ekVJp6yFdU0CVQrNMBD4J_dFylY1fgFtvkOlHxgLAhiXmBBhE0-sGER8vgfVLf_sBdlfQ6KWpa7_4XNWuPPy3Xv-njB-6mM4vKUzWZ_VZFFLJc2qz0NtNX8js_H1qJ4DEiavYsGCb1ee7oMUrZx5_F9OLu6lX5xP2QKxdy7VEBaRMpgt-Fmt1ean8F2SXtbanpWCbgfcCNX6JHTQO7tYEwnAAMLT9laD-dwM6Tje8q0BXz9eIxVTE4u9rXBJNO3MtK7ASWMnM8ZjqhHmIA7qoqDe_ldvoT7DC8HbM5w35vWz6KJWNso2iYJKI3lZG63LTgU");
        return cookie;
    }

    public static Map<String, String> getUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNzEyMzk4MjM3LCJleHAiOjIxNDc0ODM2NDd9.NPUxGhvRyioxsF_42sEXK0VlikY9U3iyjwuDWDlxap-42lRq38eyXAYoIjjKdtzPOAy1LQmJNAVBwbSr2yAWo4BNsPAbFzP3tdAKzEIhX1OtqHf10pU3XeNRPfc5FlvkjgdIymOtUOu_uWQ--Emb20MjGLrcm1Wlf1UuaLKK7WukxxL_DFoDNlA0jTZZ3Rr1NVhPxiDE3HG2aX0KanaOrtIPoRKhIcvR6xtyr-iFPKdGRrJfWX_5UvQbIMZIeeE3b4F5ZESvpzz_YNgMDPmsYdqhU6Sq_LO2UD9MtRPPO-DeT5meuDP3DSlVjUrICiQJ4pWWdkQO7tn-KQwp7xCi6fiH6b8DSdiS0juQGiz9l7eLqZEyO-qv8gFYSW2cHzrd6O33YixMH1U0RzU-DovxOy_R1fWqgJOfJdZjY-xNfASf7adtUGJm0XuWIjAbLEkdgQyFkXRWMSFKhOdDRcGEZgKhmejNg95Ulfr1AAGhu2vAyF-G_sM5-FIIGLzytg8wZQIfp4fEu_OdqkQbMg6U2u5hyQUjCR-odbt5vyUP3mxLeh3LQDg4_HK461bLnEkDVOSoSOV2qmykVtWe_6wX5xJtVRC8w0Aq2U-AQuMu8nQngl1nLN0zf6lCrnvcWYSbm9l2g2EM4XiyaQbIWEV83flNnCXlf0y1fjZL3U1GlBQ");
        return cookie;
    }

    /**
     * cookie with no or unknown role
     */
    public static Map<String, String> getGuestUserCookie() {
        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJndWVzdCIsImlhdCI6MTcxMjM5ODIzNywiZXhwIjoyMTQ3NDgzNjQ3fQ.K7ZnczeP3L9Ap4Z1l62q5rr4tomwF_rv8yyoT88J1GMnU2Oa3mT0wSPuVbg5kGrKgO3EQpJXuvSqRWqhuAbBjhexu4hBtaKc-mW37MrwizBviSy3G11hLL4eBWl6jJHBNJfoVw2P67WysuGv5LxVFLDM25w9VAq7xa0ILzSg6KTfWRTE0S64UKregL_GdCWqmNeQBU0PfzXoxseu3p7iBDd-3T2CNiLxHLMe2NuVDceiq4CHjii9ihgTyMkHkhKUF-oP_adG_jPPo8Pf1Y41uhoegV9wDa7XBEmr_Hf8dRLEGSc8tTwuBH-VTjm5HvrBFQpt3tnY2l7BOLkBaHpA9bLMpiSA89kCtF-_9VCQRJhRhVNhXyAaHxaWDogg-pxZU_dfwqiJVut2QFfIjPhJuVuaDy0ydiv_E598dJ2ioSTcD7fq4Fyco3lf-x0Lp-LCrup_mhJnslQotPVPJ6j_12YhuOziKB569_YqnWEERafxX19dWQbTeBDHXEQkZp2e8w9okmnElpRUMFkZ14tASIHY02jTmgw0cjk6_fSS-YBYIMNMVpu_edCehFWYFu5SeymcRdgteYK4dUFbdpwAmBWxL0lW6CxEJyF3sQxHwvHlxwIIi_nk4OZXzgnYJiXLwWWmHLqV22IfzBZyzk-mPtWHHMQwr8RWo3VGo4X8SD0");
        return cookie;
    }
}
