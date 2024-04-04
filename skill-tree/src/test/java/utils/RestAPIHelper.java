package utils;

import java.util.HashMap;
import java.util.Map;

public class RestAPIHelper {
    public static Map<String, String> getSuperUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsImlhdCI6MTcxMjI1NDEzNCwiZXhwIjoxNzE0ODQ2MTM0fQ.XfDnoyEBP9bTNkYwcuM5xcPgaChphvI16JWlcIVFl2BAEQ4Vc3NY8Njmp37cvS7X9Fx4pZJ4ujySe9xPsvxtfpVV52b9hfMdY4S4pL1J4GBg1cMUzb97oU7HLcKUvfD2YZw2B2ibnG5Cc8UyR2tsDrIvP0j_84DigS024p3LSkRq--AK0JxrdmqUVfYD4lZHNnmhrMbh4GDY8u5g7uirAoHbSdoUG61laQO3QGcIuNqj1PIIYVb4hV1h2IzAF_X_XrR5aSF5XRDLdMkeIbbmrIvWwnG3barpfiTTsGdT44tc4MEW0wjrx1DuY2Vjm9lgupbvWDicmrF_LfBFEWA5wsp3T0dgSyJoywMLKQtFQfZ2G6qVRomnZB4q8EbENbgyc1R42t9xjd14MoUnf9kbPbf-Vyhz4WKpQTMQGH16w6HDiiBvJnWi0Wjofzcn3rnyP4kRiwP4q-d_yVXTsbIO_u4tKhpPuE32oBDDs40b3js_wibYtWIdlogUaOuM1D4lyoik9BVM3og838QF9AIe_cxkXi5BkWpjKSjTkfPdFK36kRWefR_AsVuX5Z2IrrEGCf5dikz6CI0UsZXFf_pzc5skjUP8oAIWwGgTXjPdI2M4rlZyDnep0FbxLRBujfCYmqYlbkLgOGv9j8nNTuk6ichD6CNfO4DbIH_w2O0dj0c");
        return cookie;
    }

    public static Map<String, String> getMemberCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsImlhdCI6MTcxMjI1NDA0OCwiZXhwIjoxNzE0ODQ2MDQ4fQ.XKKiHoVHZpB4BcRCDEAFAt2U0KhTkNaiCCE03K2JoSBpLceHK5B3H-POnfapBgX-FJupW4oaY8t7C4pKgShP6MG-HPx7Thcjv5xECdo5p0WQqUOYJSe1JpzmSE46QNyQIdSDaUSp90C-12pBrbebV5W2SiMj5-FE9frJyUOJ6LjcaR6JOnsKijcIwqM4wbKTDGzh8Exa-NtZT8jRAw7ttyGGcwIQR5qGM5CRVELZEnmA40urmmGrSX5Kjqrnw5HStu6StMtLI5eK7z0q8cVetoW5I7UACcxg3w7d0zNU1TbrBq09lJGZubyhQZ3wcYaPDKbZ2J0hpQDo1jqLaZdmN77rYNl8lw75yUEMkv54J7PB2uiesO2zBDwopOkJ3_xx71tC_cTStgB-eoEUaxjSH260W0bN7WzjgujL7JWg2tTCZi6ipNcY_wRmIsenVzmdRQGA4HvEOmW5jwx0jvl0AjmAbAeLecmGRnv2e349mmkVm7QNyQWSBZT2zIkFgCCThZUXO3nVXneYQsYZEkt520A26AHGwg3SPXoiIRgorDA4DXAo5G8QG0UkU5th-o91qfMDX8zFap7wELGxqycZNUvwz7j8fOly57zGvVRrLmn9gm2n3kIgxx0agbPyPgi0tIozp38yqc3P9QaxQEfOz2H8FcGf2Me_IOXsgEHityw");
        return cookie;
    }

    public static Map<String, String> getUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsImlhdCI6MTcxMjI1MjkyOCwiZXhwIjoxNzE0ODQ0OTI4fQ.VOto6kmO5JNohlJ5NZ7dbdvpccOqbjMgPCh5i1DIvpEC3En1dwb1dIAOgUtN9GmRi00qh11VbciW_VDZiq0fsLsrvAIRmTl9DuKCRYj7T_lDm-6b0IjtooFz1oPaNM0KmUdNvfeh_D9vkKLoas52pP4O8VNmQxQ1-oPUeBIynZFukx_1FXWsAbeyh8nj-Vn22CkcaTsu4oiwuptao3i8VmvDAaEPyd0QHjyP8Xu3OAJEsjUL9cIOfVCypZhLm35B5yEUJEk3XKUDDsG70PbEXd3bzFdtPynoo5XnC_aY64DSw1iiDNGVydVhnfRm1NiTHzbL8akrl1qTqHSo6e2fRSucQZGuOtsTZm6qQo9B2gaJ5yigLoOWIvK4M7Z-UeaBB26Au7wTPWdvbVg4In1mKy3rm1K_C8_ISxqCPGPziK0iVqx5wKEvFjFddfEgtafI3DOm8Q6d0d2x-5clsnOS1c5xabAfZ-BEhsopfhlIyfBwaWKGynX1sxuSRA21HQdEhNRLgi9n57EYYQD1gGzACEaOXRtJIhQjL0RJH1Daf1iIoBrD316BbK58ftUv5TZXs1hnnmuWEuhoNJsBZLDwpjLj5_xO7qAnklr6Ceevu3daqGbAUsvaYXleirv4xS0dXZifg8-hGoicijCZDwwuvU-xcgmSB_zkATjy-07cd0s");
                return cookie;
    }

    /**
     * cookie with no or unknown role
     */
    public static Map<String, String> getGuestUserCookie() {
        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsImlhdCI6MTcxMjI1MzY3NSwiZXhwIjoxNzE0ODQ1Njc1fQ.ZuUp5cThgtITYPFDC0Rrr4iJohyBwEEoQJHzmhKWiEu567eDJ5te6HE_7rPps0a7F0TtN-Tn1g1-scTuib3xZGmwDrC2GrM_PDUFeVUClv9agVX1rBX7_wEy-UboiuNQ6MNcv8DezTYF3JEyy3ZL6GqJbqB8Qnz6fvVhEJCSCZ-1KC7mDZuZibK_cWKN63FCpVHB-tzPm9Tcg7V3jzmrBFbObTCpVRjWW33sOaHQAtwT6YItuW0FIy2n6lADY-AmOryeInLSJWMrt3F3mN48tRIGPU1hdXYy0zP9riSFigEVEK2n8dpE7j0Ub5FTsckekBKYOG8o2RHKCB1joJlKwkA1-xCf-4IrWlGAI7wN4EA37Z-Jx0PAODoBkYH8xDFhujiOENrQP0MasekgS-2OqABNVv3ry1Q4YuDfocjXBgTwQJMUCEhF4Lzdpkqo24pItTv6ouwJ0JABoc8V19L_eDZgnIzBvslOdb-dGmhZCoMeuVRyo-lFcVOuaHNjjo72vLia9IuLlA9kPRRSWz3w_YEoedGZXp3vO5exkQqz7OwCSdBTB_KbojhktkmCgLh0MDFw6ns9UWCBLEVqpOAIWJyOgro-I6CiIIAw3pGYQKIytQ6xG8Twanl2xU42st8PkF6HNQ9jqTChpYhjhLeD0fmrO4SoWaYs-FLhzawO3dA");
        return cookie;
    }
}
