package utils;

import java.util.HashMap;
import java.util.Map;

public class RestAPIHelper {
    public static Map<String, String> getSuperUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJzdXBlcl91c2VyIiwiaWF0IjoxNzA4NzgyODczLCJleHAiOjE3MTEzNzQ4NzN9.pOpm_pJk9hKqZ_JDtyFietItb4P1Wr2VvA53Qo0_nx1KHjjd24MPnQg-NOUaFZ2X_GWe8zOMklLgf3vX271tXTW98GQOiQdyqJZENtwJuxhiwT-3eLhPW8Kg2flfa3_mew0_s89e_hy3i1I4GH-YsCUaQ_SxrtlmZudYlExDhj-RuPVmz5djd7ra2mz6rsQhUMY047YKm0Szi3UMK1iWYJTHehwdtG0Y3PlR2hHyBrUFPpsxdFALcKwkV7rdcJgpDwZd6AnWCFiut940Mbr9W6tq7VMQLMC9Hjlnd5_2dVdz7NEUQ7BHTTFyStt6HdS_0-IgyZZR64AUXyMp-JAOwpLJr4A3psCc1pwn0UPy_2weRfXBm7k0xJO3cmV5x19k1jHxdW6LxaJthbsx9r_dY6B4IrsIJxBS_CefPrDv8JhW1EEkkYGWMpyzvMjqf5bpexJ1KLsKsqKIh-nEfE-CG-Yj5Cm5xFhdYE5iBdyehuClnaiBkUO4DU6GrcmgYkevsJs6uhfg_iVqZKt3xisO_0DNr1chCLCGBG_M25Xj5kT_ljzqLtaxi-OTIMQXK5lhx2e_sTrxC1T3gZ2em-WRSkC960tYfP2xnVS_DbNmk5yk3ZQEpOGgKfl7MhbJVYaXMUqR-lWLU1JoFvAsBvkh65ucztmPbXTNK6VcudWWGYI");
        return cookie;
    }

    public static Map<String, String> getMemberCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJtZW1iZXIiLCJpYXQiOjE3MDkzOTM1ODQsImV4cCI6MTcxMTk4NTU4NH0.Vn_s9kp3TZl4CUYh4U4A3fzhIeoObssWyntQrt9BajBRGPm9afZ72NPkq90cgySSxSICLZiXpJXv6RjmD84sOhXYnvAgLMNtoV0zYDJxrPDwOX0RHYU5xLFjQgDbwxy94BKa-FtU2ZznTtEb6jpdwTrTOvVa8oTwEVTcIID--mQzUyC6AYy8u6vo6BcruC3n7I-GNZtyNufxXuTAycXG5Ln7SitGonl7iU8XNovARavxPH-m_8lYNj5zwNNQwi0EdWvaMTczUb2X-CaaMeloGByZs4KPPHhAhDs-0PTRAHmYvhLrF4RpOnVVvEcNmQdAjJeC56ZIt5LfHtPLOGHxvPQstw_Lv4HgXhAPi3E8R0qmDj2DtS6o5ukMMmNxwUbH2Y0amt-q_yWPUU0spUUPwKVe18e1BPG3FAlDxh2sup-S2fvVvdIWnw2SnHQ7p7Mv2IhGRPsySEKkXrYYWU-IJdJnByHYEHAOaYFdF4N4Ssa-t--7wuYytlIsSwPpq7s0qHbptvsb-Imc7WXT8fl9_NY-SRRPJuoj7w1yW0N51nyQt_f4DtdlcBCRWca_A1OPi9pR_T4Q5LPn4VD7-2wgjaIBA4FYA7mxEGK3Gu9KCtRPxzZW-WmQ7HqPQIJLaq8RALi8Tljhp9srkiRE14BWmZ1fCzGnYCnTDnJLILuPHzc");
        return cookie;
    }

    public static Map<String, String> getUserCookie() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNzA5NDc0Nzk0LCJleHAiOjE3MTIwNjY3OTR9.d41TxVvpnxjDSAMWrcM0hWmUbHFoNpLdhZH6pJ1CV_2Q-2yUhwEUihRjKoBlkktHG0PgAHOd1oFNZVz7zAczWBKAgwarhnfs8QKyDuGHnLZeSYeFcjzomEV29vx_VOqWeha5OvmWmegvmDNFJAr8Q1QdXM_UocPrfJEc1wEHZQu4mHymu2LL-ocieDHh9Mhh4_gM7_9g03qibcCfedj8Nz_l2k94io9QOD-qfHuQjw12h5XDGTqFvk9S_UdWjSJPXl8wFq8K9bMcNU_QVGH52D-6B1n5nICfKExnASCWzGIVcaLnq6PfTPRm2SVlmuyYWc2AT3M_lPFeWwhSIRhG4UB4vMsVTBPx2HYCwB2Kt-wTa5B8fJwxQfKqZ5a5TaJI4nhkJD17BBn_cKtNwuhNVxREwNU9JLo0qzjvaQtoMJPj8XGcjGGbpIWbYo1WkklQ5ybzIV-WNtMoiJHkTxiMtZl2RBf2zXKE1XdvbW-cFtpKfh9ZVYkXJS9FDKWEziJWREECu6De_rxl3-1DTDwe2zeC3MSueOXyy8VMwUc7gQdZlJU61I9Ci0k-xYKkOo3V-eZZ3tyf4wKiYKLKe3wzAkrGvsnDVC65rFGHwlwPaHFitllBE7yjSBIdYrXkjTudNbkYe0rVX_LliRgvmin5VFqlqG8Mgg3Pd8YIXhbzqIk");
        return cookie;
    }

    /**
     * cookie with no or unknown role
     */
    public static Map<String, String> getGuestUserCookie() {
        Map<String, String> cookie = new HashMap<>();
        cookie.put(
                "rds-session-v2",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyZlFFbVgyRW85a3lhM3NHdlJMMiIsImlhdCI6MTcwOTQ3NDc5NCwiZXhwIjoxNzEyMDY2Nzk0fQ.pH38drdGYItmwrd67U-0WeN0Qs1wgR8tsuxfsW7t9u0jWUK-DtNwV3RqQfAC_tEu1RKgcI5lXPWrZW1KTTP6gw4GcRIBsYBYMMQUbujkA7gfwrX-DjbI_deUpWQHrzR6sOquikJ1AKEBoUh5PzBV75Spnj4jWqmzHHFJJ_6IYv7ppTB94zFgnS6QQTw-VWJURvHzPxxsDmZ60EtO_XkOHjxKX1hwLXfAMIuQL_I6Eb7D_irMLpfaF3LNJnQAZBb8nGsMocZ2knDhX69y7Lrx0qCvdT4stb5YrpPJj1OTOm_1rL9wLHOc6qRPYXnLYG6YDzHgwg6BvYFX3xEkrZSQ235b0y4YN_kB-QiW_jLL14zOuXm-bOWofHbIoD8o0EwIgRpWkeoQVFwbujpwjjslB2i4ohWLmpkLEieONouPiH8fL5Sf54SgxC7wFRq7DaBW5H0WPup0uGMMjCvyTIs2VQMwptSbIZ0ni82Be5vYtvmaPQCVGwFOkEEZQZaL8imILkm8_a3GB27vY5SvIbs_AUqubMu1KPYPXpuwSBeApCQctbCjrE5X_DvmKuLVoVE1ogW5KtgHlWmR5T2Yq7Ie91kJIpm54iMLZ48BbkSeKUk7xtcHwLQVBiHGQgw4xZnCci4qBlO9q9kGWFRKdaCIrmDF9I9EKU3zKmhIsejZjF4");
        return cookie;
    }
}
