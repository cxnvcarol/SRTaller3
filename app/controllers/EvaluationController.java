package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import recommender.ContentRecommender;
import recommender.CollaborativeRecommender;
import views.html.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import models.EvaluationResult;

public class EvaluationController extends Controller {

    public final static String[] most500Popular={"kGgAARL2UmvCcTRfiscjug", "ikm0UCahtK34LbLCEw4YTw", "Iu3Jo9ROp2IWC9FwtWOaUQ", "glRXVWWD6x1EZKfjJawTOg", "ia1nTRAQEaFWv0cwADeK7g", "PV5voYSD43Cn_3gHmxG7DA", "fczQCSmaWF78toLEmb0Zsw", "DrWLhrK8WMZf7Jb-Oqc7ww", "3gIfcQq5KxAegwCPXc83cQ", "uZbTb-u-GVjTa2gtQfry5g", "4ozupHULqGyO42s3zNUzOQ", "90a6z--_CUrl84aCzZyPsg", "5lq4LkrviYgQ4LJNsBYHcA", "0bNXP9quoJEgyVZu9ipGgQ", "-_1ctLaz3jhPYc12hKXsEQ", "9A2-wSoBUxlMd3LwmlGrrQ", "wx12_24dFiL1Pc0H_PygLw", "_PzSNcfrCjeBxSLXRoMmgQ", "VhI6xyylcAxi0wOy2HOX3w", "joIzw_aUiNvBTuGoytrH7g", "XjfKBr96h6GgrfkR_q6gIA", "4G68oLRY3aHE5XUt_MUUcA", "y05bv65OCTcZgB0GhO8_sA", "pEVf8GRshP9HUkSpizc9LA", "EjhVxYFeMUYimoImjMduzQ", "nEYPahVwXGD2Pjvgkm7QqQ", "WmAyExqSWoiYZ5XEqpk_Uw", "C6IOtaaYdLIT5fWd7ZYIuA", "oFhtzLhXS1iENWoBMdWk4w", "LP0bl3GKToABt3lnpSvA2w", "lpZtfD-vCqUER1yGKEvUPw", "ST8Yzlk2MqKlcaLqL2djBg", "k5p3YP1ZjCa8ZS3xqXgBug", "38JK-SfO9NkAGs1RwlH2Gw", "JffajLV-Dnn-eGYgdXDxFg", "i8KMQX7nekLSCzb_DVb6yA", "1BW2HC851fJKPfJeQxjkTA", "2pxcprc3GGAeI_RM88-Cgw", "VPXgY9lGJF3XC4ZpusxNuA", "s9I4KMcrHKki44g_eTrYqQ", "tdOZQpPQyhjHIL4MnrIKHQ", "bAgO9_hnkDjlFAkUhCSwMQ", "5Wb9S2KRzavF19g6ATpWjA", "Mx-vxv_V-SQCe76w4RmUfA", "lPaYMDmJbAnv_3pmZH_inw", "OaFcpi3W4AwxrD8W2pgC_A", "17LPTsu4RgqTZ3SxsGzzxw", "q9XgOylNsSbqZqF_SO3-OQ", "p7dvahRPZIWL7T6pFA_0dg", "ifFTbGgwjXALO1PgMHzrTQ", "oOayyTrVO-PbVeXXsh_JEQ", "OAd-vbR_POac1zHtu-Y2Zg", "zTWH9b_ItSdLOK9ypeFOIw", "iTmWHtltCtk0Gm55AOxrUA", "rzHZ3iYVQe_8h2e42DbUsw", "Qx9MnE4R-g70HyF468O8dA", "Kqvfep2mxS10S50FbVDi4Q", "4p-qAdc_ZLXfieNwaZGNGA", "wHg1YkCzdZq9WBJOTRgxHQ", "8dbRf1UsWp2ktXHZ6Zv06w", "SIfJLNMv7vBwo-fSipxNgg", "T7J9ae0wTskrI_Bgwp-4cA", "P2kVk4cIWyK4e4h14RhK-Q", "pAZYqb6OuPjlHF7AeGy6uQ", "XpxK7cAT4R3wQjHOZccasw", "0Q2QhRuyD_oGodCj__wcvQ", "XqMkm-DD9VsdcKx2YVGhSA", "m7vtKWpZ9wdEQ95wJxrMrg", "KkEFlQAmrKf6BtPVi6EU7g", "Ps1Db9zOatoF_76FZNO5CQ", "ts7EG6Zv2zdMDg29nyqGfA", "NzWLMPvbEval0OVg_YDn4g", "thdVzCfKx-DV0zYWqId3pw", "M6oU3OBf_E6gqlfkLGlStQ", "PM2jXrlVzik1jDwwahLJJQ", "rLtl8ZkDX5vH5nAx9C3q5Q", "XHkzCzmIAB3DnjQO7v6jww", "CvMVd31cnTfzMUsHDXm4zQ", "UCQNrwN4_hHHTWYa8cqe_Q", "c5baNLWzuQG1rm1wigyFuQ", "OTQ68fm6EtcPD2n0g6Y2mQ", "kG8jkVFgOAOq6Ht6RvAjEQ", "8p4at4zdzCpueAmSBaorZA", "qqtmFGhyyHc37coT4qJxaw", "z6qJ5ZJz979Mpv7eatTIHA", "EP3cGJvYiuOwumerwADplg", "C8ZTiwa7qWoPSMIivTeSfw", "2HmHgW3hRYvXYFmQyQtLuw", "kSVYpNWA19wUplbdi0U0Uw", "NjOYSgr2LNMoSPi4e140Ig", "WZwfmfP5X1gDDeL-M9vzZA", "PpkKVodWC0sdn74TbHQLzA", "lC0KGXmIhyjzghBUlVnkhQ", "I3QDSkHyHUd3wlmRUb0eyg", "QYS29txRosYV2mGu68Cnhg", "8JC-Yb3UDUv2FUl5ym1nVg", "iwUN95LIaEr75TZE_JC6bg", "GrSixRnGIxNUJ1Cn5DNX9A", "usQTOj7LQ9v0Fl98gRa3Iw", "NfyrHToGY6aJu22U9oS8Xg", "GI_0tXuL7dWll1m1i48eQw", "vsXP832M0kOxKpfduD7dWw", "NvDR3SPVPXrDB_dbKuGoWA", "4E_nPWw89FLFHdNsEgMH-g", "LusAw6vTDC7KAfbuClMReA", "PShy2RYNadDUhJf4ErOJ7w", "JgDkCER12uiv4lbpmkZ9VA", "6OWHDbEq23f24Vki_veHBA", "XgIhw-aWaq_Fx3ZVQGjnuA", "C0UY1f2BUUmLdmB3sn5_lw", "Mb0psF4WQF7gZSuZafVr-g", "7DxQDfrnoQI9nGALyi-LyQ", "y5_vrqIylhXMrejM_-x_vA", "xQpO4Jfi-Np3HLmJrQAe0Q", "FcUVfATxARfURHfZq1DNTg", "tdNV2Wb0LrFpm1Yfov_Klw", "7C-jyTrOX-iJ-MrSJuh-8A", "OI-VGo5ij4TMHVw8xWrKNA", "HOaWRV72ZQs0me1geSqBzg", "PuW93FmHmLaU1hrWOXrvjQ", "pQ6ij6cOBRB3yJGHbVZHUQ", "kffxYQVQsEpF1DVlQlcGCg", "W_QXYA7A0IhMrvbckz7eVg", "AdFpuH7MSin8cKmpT3IbCA", "qXMObUokAEAkgUqD7Oc9Dw", "Cp-PV8rsypbO-xBrQ6KmQg", "nHsj0cHOiroAsmDfF50BtA", "f5vgLcoKpFcTvD4lOUxcTQ", "bCLFdVfkAiZ7t1xDskb66Q", "xV1hf20ZFDSoaMZmwLoHNg", "NNdfPF-UhvLB8FxbsuSC5A", "X5GbNXY_nNoa_vTZDD0aCA", "Wuo1WKSgEt42fY7NxXcJYw", "LEL1_6acicC5d-HFZ02ZLQ", "_vS9qNQvx8wVUOIkj4qfAg", "FbdB9KXyuvT8rC_zmqRSuQ", "jdeNI5TTTuM6mj3HTgstRA", "yj4lNvmxAt42NvBCxB3WTw", "9FDMUyJStI86B07bKQleow", "Yo6WTjxu9mnitbm4639owA", "l53FUDHRHLg7BQ89KgAtxQ", "XBHJCDzNh--mBhJkzyM_iA", "wFweIWhv2fREZV_dYkz_1g", "sALB8-F04S9VcZMF_GkGUA", "7x3-B0yaO_hqXm42VKStzw", "Eshh2wcI4SHdj9UJ024xyg", "4_YfcfHkCem1onJFkItxjA", "Yw-Q_4QrwWffjnHWLvo4kw", "YCW92r_ubnb2KpY23VSxIA", "dn9DSDp9CYCRffmL1voq6g", "So32N7bSbUd1RwhFtI6jTQ", "OXzYyZSHo9Dl6Qd0s_o2Ig", "MWt24-6bfv_OHLKhwMQ0Tw", "tZs84cKAUSOtP_nAiSdreQ", "WDWFOxsZrKC8kT2LBQ3BZg", "APLIPfq1Rf8QyhHHk2uAyA", "zCC6huLkNBEr3JUgQyxJbg", "sjpVZwAX8gQSH82AGyig4Q", "UD7Y1CqfY6mDmRwIuCf6nA", "QMTQNQmp4hQ2o6hkRX_Uxg", "Bmzq_nkjYqp8aWn12RJdhA", "6kmu0mYbdpMIOZ6Y0eVsxg", "KJDymAcoPiW_vuqthGxfpQ", "pzmzWFoVIKpuYYG_b9bgJQ", "O0vvS6arf6WlUfTOoczcEA", "wNqwKWaRjClmcKsoJJRFow", "9SZBVZ-mxebOVa1BlWFtWw", "KRI1kCT7SY6XMX9fBALQYA", "-K3ZjROK0ml2P-Rk7ttHzA", "7GC9fVWKa4a1ZmBGLH6Uww", "vhxFLqRok6r-D_aQz0s-JQ", "cxTh9xxqbs-m8Paq6jAumg", "tHz9cWxwSfcP4qRaPIHvww", "vI7wZxTprpMl2fj_IHU3iQ", "9cCTmiJ7hz35rHIdr8n9kA", "N7wNlo3_EaHUWHiFDyGh5g", "wY05e7eTtMLvkg0bcZ6zwA", "1pL-KrRvHcypkMVLd0MNtQ", "AeucYo8J-rZjcq09Wuqsjw", "EKJVvRrFZiBjbkp6OcF4AQ", "ky9pQ8AKufvZ63IxR41jGg", "kpbhy1zPewGDmdNfNqQp-g", "Iycf9KNRhxvR187Qu2zZHg", "hFtlFksrcLaWHGPNa6SmeA", "7zDqr2I0-xpw9HF5Ha54cA", "vE4TIv-AEsFalZ4faTRH-g", "7ewE_dDxjXNppOj0EhhMIQ", "do-fO-EruYVP_FQPMNWmZg", "TQ5eVGOFr_qB5_BbEd3Sow", "d0wJVf7Gv7v1B-HHYhxZtQ", "zdPRXhhJNCiCOgWQwoPlpg", "NwwyfANxYkjK6q54qhGV_g", "0zVI2BY9q4ry5u5JhIlZyA", "Yz5sgWyDo8EmWQThEUPQXA", "0XRFyY8-dxmrDheFPpe8Sg", "kJyR4gT1pfCcNjEY9-YMoQ", "qASPib1Z8ft8e96dtbh66w", "sovduOweKgPFAwA-MmmtzA", "fRbqoHAkgC5MeW3x15mezA", "nc3cqVN0UuB3m50-CcMftw", "jqVeTSin5GeRm0ceSg-PBA", "EGiAtB4sgZhDdYpRkDneig", "gUr8qs00wFAk851yHMlgRQ", "auVkTXPmA8o3oU8barlBSw", "VzAHWXVOIqylfCtIRXwU9w", "q7MrNVt1FE23rwtWmPYWHg", "4P1AfdABykulRK6C-ww4sw", "HOleI3jz1MLNUJ6cc1x0Pw", "LqgGgWi3FLHBViX9tmZ9sw", "ChgmBht69bnqSdDxpWMAqQ", "Ovpa3S8xD96dLE5eDxcxJg", "fev0iI-XDrteD4SYRKjiUw", "6ds42aOAl7dGgZraeAlByQ", "rO3WEI9L-_deUR9-JHuNQw", "tBvrnSCLSpUdCDm5w5GPkg", "bMiBuaaYZ3FzGWeRJ60c8A", "ru8p3RTlk8f9LB_3zLXURQ", "6ts41fCsDKHbFZaKOMNmVQ", "du6KeE54IFbPiXpU3LOd1g", "-xPKyCJiK9q1OFZ7GCZwBw", "QlOc_cKy_7Fs-Pg0vi9NAg", "zbUpiea2aiJSKXjj2zQ5yw", "M-O0tasOl0SGiUsxdO5cZw", "mD1uXKXoCgxfpf9wz_1utA", "9LwyHtKt5iRqlcFH-897aQ", "0o0VMEJeQY0pAAZ9nxErBA", "Qe1l1ln6bf_pcWUUuLswFA", "eD9VAnbyW3a1Vg3bfLoX9w", "8ED8BPCXKXZnGsVMyl5Bhw", "UsULgP4bKA8RMzs8dQzcsA", "_uL7OiQSfNsCd60DrAf7qQ", "y7sq26EXGz7XG7DSo8TOuQ", "A3mpjzazkSDkua4cPSayYA", "1X4vP8jG-EhpkF73yuJPmQ", "KCQUgDM3vNuB5xn4ZXVo-A", "OPStKHloEOVxcBtFgkzh2A", "RSExP0-Boucr4QyIEzZi0A", "gBoMkFuGtVBUpJqtRL3qAw", "n0GH-IAovki1Zm6bA2fCrg", "IBJwYcDz-bgc2zd9IyN7bA", "h4RkUrmcsMc0VTUCdaY4_g", "mgViQiuQG_3d2CmLTNW9rA", "A99dyhEqcd_yXKPfBWeZHA", "_X0Xt2z5Q7GESxxRzvy4_g", "5zbaSr7qSPwb0E_Bzy64rw", "RxfFQxUtSIGYwTKU_rvAeg", "F6QsMoJdvtohlbnST-fDyQ", "gcyEUr4DXcbjnGRAWFtfAQ", "3m5rsk7xoLddGrLK3Qi5rg", "ri0xJJElIs47Tm1xvn1dOA", "aj1atE6H4Jf9mfsHvWhn2Q", "f7Hv9J9GTTXd3XsfRgt3Pw", "Vi6yC7XiZDADYG73Z0MJAQ", "wUEp87FqOB9ew27Iz6zk_w", "Cw3UMmYeqqx1u4HIWaWw3w", "Xm8HXE1JHqscXe5BKf0GFQ", "M4MVVzNPZHiJdnNvWjL8VQ", "Zwtx7FLZcK5F2P0-Tz8mHg", "7_XwjOebd1temr3CaqGwpg", "bvu13GyOUwhEjPum2xjiqQ", "hWD_PTfJUXjIYb1UJ0wNFg", "whKpnugMnWTz3UjlrN71yg", "A_O8wZOsMTPwyeYA4-Rsow", "hl9fXDbO_F4vwuwLn6j01A", "E5qFR0RBQBoXx8FbrOkdbw", "_4lqpCYCqOQzbB6xQGGhrQ", "W6LTsXcY1R76M9EdeN0G6A", "GzCK2q4opam7eeyqlUDywg", "p8bkZSzubV3KAmduUIo0lQ", "D5mlAUZMCQ8yc0PxOgGpsQ", "UPtysDF6cUDUxq2KY-6Dcg", "Yk37VAp452K7FwHWd1rrQw", "kWkgwRxqWD-h260dMTkHUA", "3KCu4c1IOZsUla9pqQc48A", "DJWC4idmjq_FmcOCbTljLw", "jp5lBxgdCpDRhSeEuRK_kA", "J_q8jN623uew8ZzMChXBCA", "U8l76MT2Ej32cFKfFI24fA", "dZXlBNGaTXd3iiKIWcdjwA", "D1G8W0SgCbsKTv7cAqKcNg", "IO3AsR6cdMto7VCwfPzf2w", "rjlb-7-JcmM6fR64ZpyTug", "IjafRfMSAQpInLlw-Vi0Cg", "nDBly08j5URmrHQ2JCbyiw", "Pv7DGHzZ-uqIUdOsqPpsVg", "2bAbL28lhrnyOlYiZ8yMdg", "fwEPqYiyZCaB7QKY6a7h5g", "RvyBcMVefE8BADMo0Dl48Q", "lhMo-dGq0V2iKqBIiwUJSg", "huKYBfeFtYCOWs4I-GESSw", "5G0GcGQU7NaPghLBwWeL5w", "L9-X4KASFfdhOeVeYSDgvA", "N5lS1Q1GV1Od5PXmP35M3Q", "5hBsVMJOc8sXMPt8S3NjmA", "Ki71iAFTxsPPQfX430c99Q", "H_JsAwjiGEOr_RlZmwfNsA", "3Ak0T_UToT5ViQGCppiQHw", "J0SEk_wHYZkqJLRlVcvRnQ", "CWw5C2I7Vz_YYI43WId4wg", "N-D-d1Z4UybdlkK1HxlNPA", "SHcX9hp5RG3-OqsbvissIQ", "4LOUTfHA1tZQ7qyNDLQuow", "qABKRj1ggJmtvrL9FMFtuA", "aTi0NVrcPJWbN6jAsJVcAw", "j_R81tns71fV58H65wShkg", "yuPBS3fak77lgwO_9SXVYQ", "ODhV53XhesCBLJVfGsJGNA", "sBIPFP5uX8BhGvDrgqHHog", "yf12P_T0AKLNzFt12zj16g", "rNGT6lePmm803H8w0rw4Fg", "DInIFTmJWQR-SYL7EcPdbA", "VsGlXCCjzsKyuel6Gpu-7A", "uBp2Jmip2qXQ0iWHUDY9sQ", "gzJpPaHN-NXBkAZcZri3hw", "pw-k3TMjt1ZNB2-iRVTDlQ", "60i9h-gPKH1f0YC_bZXDhg", "-hwN2juKYn-KTxa2WBYLnw", "5pn0Rb0RSvt7IkPLWsGeZg", "0auxKhvcY_-4Uul5eiUWwA", "EhcZKrQJIS226xdm_qJ71w", "23zYCcnqfru5jdxnTqjgDg", "H1tN4ITtiMj3UH83OSviiw", "YLE6GoJ61fb0QBF7JTkaJg", "lC4X2crUuxT9Ac9BlOX4Uw", "hru2IDwSyN61sgWPtJw_hA", "KmZ9l-NoWdJJ3CG_euUJRA", "YslVzCJPQoeOwqnYduJlyQ", "GubdNFoDAsiwE6bWIr97cQ", "o8J-ergbGYjrUy85I_rxzw", "jopndPrv-H5KW2CfScnw9A", "fl6oI21uXoxVMwfR6lFanQ", "psf7Etrt3azIdhMhoPKmpA", "YlpzPPySUxJsUJHRyRde8A", "M-TwsqjrGVH9-qyw2KcvdQ", "gsdSJTKtaLtjynSD6hgQVw", "-ZOyUKJ8d30debwzj1FN7g", "KtNT8biTJlbAFh-APgO7-w", "bRIk0mT3GU51Zsrn2UpKIg", "hcnHQ3NSgtASSAJHrD4ZxQ", "mDS8rlDOcgRT3PMHGz3tug", "IhcTLLWKS6ZiNyIX9oQQPQ", "AirVzN1BVQBmq5zY0YMNYQ", "kZ11FAE4NGbddxOOPeRL5g", "gqkKPS92L_qG03gHaEtIsQ", "ieaT3v0coUNrSqVgY342cw", "ZPnjxhKUsBDhXEOBKY_n2g", "n_lAaNGeWgspbbCJc0DqRg", "J0h_oB2VEhMTlcqKmspjWQ", "33vUIil_GCaT92aUaZhRXA", "IGkoathSlJmIOBDGo9vUZg", "VUzGMSH9u1E3h4QxaIl6cw", "Ot6PMbyWmzYMYS8miApFAA", "KMXTYbd0U031Xs6ygBtWNw", "ER53FoKMKsPD7D20cH37Pg", "BV8c4QZEzvPWA00ZqJp-jQ", "45V7-1r79DEG0HbuaKYIlg", "e4TQFVfepzHf--hnBsjntg", "xdWsnbaTEfBMVHttqZDHxg", "ZC49A1K-if7KP8VJXoqELQ", "AkJFqLqHHAKY3H5R8p7cPQ", "VqSCQCva71Q-ZVcMsYfQuw", "OJsmh1DbXlvXOBEbCzXdIw", "P2FD7E392y01VcBVWli6og", "KzNEZ3o0MBvq3fjjbB-Ntg", "PRcfYRZGxTPFQgFqpnvp0Q", "ZTijjDIAUmGq1vyY0xloqA", "sMBdzt8FE_oaqIzYRAwWZw", "IrzHAVpjBUSLnnrY5jG2OQ", "5mHPtvzOpfT_FUp6F-AJKw", "7cR92zkDv4W3kqzii6axvg", "e8DXpXcwOPdP25hG2IGwog", "cUjy7cqOxoqq5l7XrNx4BA", "Uchznb_3tmqIpJTh16iRPg", "IkC2BKzADqgPlvaac5gRHg", "X36p3mD4oqBE53sASc_Ykw", "2l2lRFuHLdyGjAuusqPDag", "yMEO4fsNxLpuFsZI3vGjPQ", "PS0lCxjGNeUrKxYSdpW-Aw", "KucBnMrhalzxnD9AWrxwYQ", "ITwewV_k1EcFgxfWNlssqQ", "dhVuDuzCimescgn83tWQOA", "3GteArKIVRlJZjjRqqwgaQ", "pv82zTlB5Txsu2Pusu__FA", "-F32Vl8Rk4dwsmk0f2wRIw", "W_R-gVF8OHNrIhtmyhDnyg", "h7v_M_0-YVpSVZ2WD7FpAA", "ZmIgP4U4Ht9CYmNX0_zP6w", "RvweNJFVkR3ttkWsIBy7nQ", "d--blIwz8HW_srunlGbufA", "qXLte7ikUM-__NHU5RaUgg", "4D4UzEOu-QG4-jkyfynhPQ", "bwmXfjwrogAaGqV33kSVpQ", "C3gwYFhIqhdubl9OZVjBew", "5MhbO-FbPPRkM1G9oZsdOQ", "gNCf30Aow5gAW7iSBcV7GA", "xj6NkHKs40GSnhRrWOuzUQ", "tmuVweJdPKDnZGnQOq2L2Q", "E0ztmyXb3pcVABNqBmURVQ", "cBJqlNzyoJFak3_XRe2bvw", "C5hDZleIfHtcXS9p4ZYBuA", "wqQ-50Sjf9ol3SYaPbA1YQ", "qa05pUVNapADHZXpHMPMeA", "xFG4Ca2HHmbxDTkMlmHnjQ", "6xrk6A61OWwOtmQAErZTvA", "o64IkZLYluCHlDtWzQE71Q", "lmiDCrmas8TxRsbIGZX9Pg", "ROr4DQ-gxlfUKmiDC-qSEQ", "K0euQLFAcKhQPZ6JxGAryg", "diDUDx82VePOC1hObkHwMw", "vUWfWfGW6L_DVKHk6I5YYQ", "WzaaorVCmUTQvu4mScunNg", "iBO2m9xXivsIbvFnZHsNvg", "SEDFpR4oMPKqXMjbJiMGog", "sbvSyNtI7Sgp7k4qzf2reg", "SKyMDoDYuPvdy15zOkdeyA", "b2DKC4kC8-QeSeGZ_MF3XQ", "3ZnCGqeaJP5VDXnx_roIXQ", "UoBfwbXaixelZBghbJ2cSg", "fPHLPrymsyb6WSFFKoMrTQ", "Xzl3sw1hZya3_e2niAqxZw", "9OZH1Ecw-qUkCW5MS0NefA", "pIm17SVRo8IdhGoBD4qbnA", "FjDJcEFfPa9eHxmU32vcLw", "8-mw_aotUGmP0k0s6QKwOA", "wuCszpVPMIMyOCualnZtaw", "ED6n40WmZJm0AvsKkG7iaw", "k10hFpAJJk4gGxEqPbhg8g", "ZcEstCa4fDhNXrkZ8_Gh0Q", "mlBC3pN9GXlUUfQi1qBBZA", "Ry48Bta6spNowOkiMNC_xg", "k4-6lakq7t3kCgbvPxcuvg", "Psh8PaEh8c2Wn8YjwyGVvA", "Q6FaSZpCtVv7PXECwDL1bg", "Q5qn0BUEsAHoidP6wUzuSw", "Hi7VohxSS5OBt98w4j8t4A", "Vi77s0AS-cIzPoBq5AbEaQ", "ou-yGrDISZPfe9gDqXMIXw", "3sWSeGT-5fQ2Z6uAW78VMA", "lTR9GsUJ8iJqH6yh-51cmg", "hn_DclKLzdxABRGGAuqKgg", "TCEqENMcnIjsBCjlI_Stzg", "ZWdsr06AvPgDzahZQ7hd2g", "-kIvLyWpY17aRa0vPp-RmA", "KSTOdz0lSFCkqBSRNwjERA", "3ONPPazUH-Q3XblYJIaA1A", "4IxLt4QvCsJEdaNvH9QApw", "xNb8pFe99ENj8BeMsCBPcQ", "CInFos81Gf4dkBJQL-1jGg", "WWC7LdZal4Db9GKXLyIhPA", "A6bPFcUjuuayRBoyybedDQ", "D5J4mKahKvUBJUTWanZwdA", "8cJ-n5BEb1LAR0qFM9zrxg", "K6MrbZeqKG35ZiucFfucvw", "dFSg9ZUwdvXO1c1ZACgZzA", "9uaXGnqYUvll_Z7ZyWN-gw", "6DuBPBgCN6YMowFwrp_P5g", "Vlon2bJdT43_1oHSrQsHTQ", "Q3fFv_ft17OyV-NRF1iQxw", "0hdkW5qaQs4gTiUnRdadqA", "weYT-xJwz8o7mLNWIhD6HA", "zfysCL-0uQNCWdGvzZ6QWA", "KYeLSM3MQpirmHukVV5Iow", "nP_vZUqkoajeBC4qTV0Mlg", "DOJM58OkGSsIdk2qCUZnLQ", "Pk5T0OFCbl8mIKkKYyglWQ", "wC1CPV5bf--bDLtlhNSPLg", "GLJazgesK2b_mRYQaEc8rA", "DSWiwtUyWK1YwY8NBKDhGg", "y3FTaotN0O-V41cBiZXnFA", "ePxzwqgNLQnYoObzLYhYvg", "DcgO7qiYKS2VuAJj2dQpcg", "_lmMNw0STDLEI1_qEro6Mg", "eFxX1tSNLmVkAP9kNv2--Q", "FDG85sQMtHuI2a0ix9ZBcw", "R6lRupz_Jo51n8WJGJYdOQ", "zhVOlwBuEgdGlHjwgVf3Jg", "fbjk9jZjru5xQw5a1ESzXw", "N4BnjZFAuuAUTjxs93SYlg", "G72bs75BqgW9NAVv00ADTw", "zHfA7hsEDI2TbPJ-nDD-QQ", "cRyNICH0mhjxagvSyVr60Q", "xZvRLPJ1ixhFVomkXSfXAw", "n9Zg0jlOGtxfIrvoPm6Hhw", "2KNPtV5E44vAiEr5BvMkUA", "LK1POYLdvIBWzbqDxaxKRg", "palND-kF1qpMLhkcgAnSxA", "ZhDUVCThOzw5LwVQ4sR59A", "oLAbWZXHwqOdbnVo9_mfww", "RMX11FC48KZTYIeExtGUCw", "yRubqnQy4ExPYTel7KsNog", "7bdCfESkLLta4vwxO5tyHw", "wGWFzdgnSEe7C6UjoW8Q3g", "sgkRdA1a1Vymdp83Qbikig", "Z6mx5OClrvg_C0xBW34kxA", "wbIU917ctOGp8-NdeVxIWw", "jIDhZ23eeUzFGPOxgxG3Qw", "VeN1wG0J9lJuv2R8EVWllg", "AMaiA4EVbEQ4tm1FsYPdbg", "LmbJD2GU-FXYhYoAeOWhxw", "qZrArI2ohkYkQlELl13BKQ"};
    private static ArrayList<EvaluationResult> precalculatedEvaluations;


    public static ArrayList<EvaluationResult> evaluateRecommenders(long timestamp) {

        ContentRecommender cr=new ContentRecommender();
        ArrayList<EvaluationResult> evals=new ArrayList<EvaluationResult>();
        System.out.println("Content recommender evaluation:");
        Logger.debug("Content recommender evaluation...");

        EvaluationResult  res;
        EvaluationResult resCollab;
        long t0=0;
//        try{
//            ///Params:filtra por radio, #movie neighborhood, # users to evaluate, #recomendaciones x usuario
//            t0=System.currentTimeMillis();
//            res = cr.evaluate(false, 500, 0.3,50,100);//test control
//            evals.add(res);
//            printEval(res,t0);
//        }
//        catch (Exception ex){
//            Logger.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try{
//            t0=System.currentTimeMillis();
//            res = cr.evaluate(false, 1000, 0.3,50,100);//varía #movie
//            evals.add(res);
//            printEval(res,t0);
//        }
//        catch (Exception ex){
//            Logger.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try{
//            t0=System.currentTimeMillis();
//            res = cr.evaluate(false, 500, 0.7,50,100);//varía % training
//            evals.add(res);
//            printEval(res,t0);
//        }
//        catch (Exception ex){
//            Logger.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try{
//            t0=System.currentTimeMillis();
//            res = cr.evaluate(true, 500, 0.3,50,100);//varía filtro x radio
//            evals.add(res);
//            printEval(res,t0);
//        }
//        catch (Exception ex){
//            Logger.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//        for (EvaluationResult er:evals)
//        {
//            printEval(er,System.currentTimeMillis());
//        }
//        
        //Collaborative Recommender Evaluation
        CollaborativeRecommender.generateDataModel(timestamp);
        int relevantThreshold =300;
        try{
            t0=System.currentTimeMillis();
	        resCollab = CollaborativeRecommender.evaluate(50, CollaborativeRecommender.EUCLIDEAN, 0.5, relevantThreshold);
	        evals.add(resCollab);
	        printEval(resCollab,t0);
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        try{
            t0=System.currentTimeMillis();
	        resCollab = CollaborativeRecommender.evaluate(50, CollaborativeRecommender.EUCLIDEAN, 0.3, relevantThreshold);
	        evals.add(resCollab);
	        printEval(resCollab,t0);
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        try{
            t0=System.currentTimeMillis();
	        resCollab = CollaborativeRecommender.evaluate(10,  CollaborativeRecommender.EUCLIDEAN, 0.6, relevantThreshold);
	        evals.add(resCollab);
	        printEval(resCollab,t0);
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        try{
            t0=System.currentTimeMillis();
	        resCollab = CollaborativeRecommender.evaluate(50,  CollaborativeRecommender.PEARSON, 0.5, relevantThreshold);
	        evals.add(resCollab);
	        printEval(resCollab,t0);
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        try{
            t0=System.currentTimeMillis();
	        resCollab = CollaborativeRecommender.evaluate(100, CollaborativeRecommender.PEARSON, 0.5, relevantThreshold);
	        evals.add(resCollab);
	        printEval(resCollab,t0);
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return evals;
    }

    private static void printEval(EvaluationResult res,long tinicial) {

        //Logger.er

        System.out.println();
        System.out.println(res.description);
        System.out.println("recall: "+res.recall);
        System.out.println("precision:"+ res.precision);
        System.out.println("averageTime:"+res.time);
        System.out.println();

        
        Logger.info("xxxcontenidoxxx:");
        Logger.info("");
        Logger.info(res.description);
        Logger.info("recall: "+res.recall);
        Logger.info("precision:"+ res.precision);
        Logger.info("averageTime:"+res.time);
        Logger.info("");
        
        try {
            long tf=System.currentTimeMillis();
            PrintWriter writer = new PrintWriter("cr_"+tf+".eval");

            writer.println(res.description);
            writer.println("recall: "+res.recall);
            writer.println("precision:"+ res.precision);
            writer.println("averageTime:"+res.time);
            writer.println("totalEvaluationTime:"+(tf-tinicial));
            writer.println();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Result evaluation() {


        List<EvaluationResult> evals = new ArrayList<EvaluationResult>();
        //evals.addAll(evaluateRecommenders());
        evals = getPrecalculatedEvaluations();

        //evals.addAll(evaluateCollaborativeRecommender());
        return ok(evaluation.render(evals));//"Hybrid recommender system"));
    }


    /**
     * Those who have review more
     * @param nusers
     * @return
     */
    public static String[] findPopularUsers(int nusers) {
        //String query="select user_id,count(user_id) cc from review group by user_id order by cc desc limit "+nusers;

        //List<SqlRow> rows = Ebean.createSqlQuery(query).findList();
        String[] ansa=new String[Math.min(nusers,most500Popular.length)];
        for (int i = 0; i < nusers&&i<most500Popular.length; i++) {
            ansa[i]=most500Popular[i];
        }
        return ansa;
    }

    public static ArrayList<EvaluationResult> getPrecalculatedEvaluations() {
        precalculatedEvaluations=new ArrayList<EvaluationResult>();

        EvaluationResult er=new EvaluationResult();
        er.description="<b>Recomendador de contenido</b>: {filtra por radio=true,negocios revisados=500, porcentaje entrenamiento= 30%, #usuarios prueba= 50, numero de recomendaciones x usuario= 100}";
        er.recall=0.001948947093;
        er.precision=0.0082;
        er.time=8070;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador de contenido</b>: {filtra por radio=false,negocios revisados=500, porcentaje entrenamiento= 70%, #usuarios prueba= 50, numero de recomendaciones x usuario= 100}";
        er.recall=0.002658690595;
        er.precision=0.0048;
        er.time=10739.33333;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador de contenido</b>: {filtra por radio=false,negocios revisados=500, porcentaje entrenamiento= 30%, #usuarios prueba= 50, numero de recomendaciones x usuario= 100}";
        er.recall=0.003216554959;
        er.precision=0.01353333333;
        er.time=8638.333333;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador de contenido</b>: {filtra por radio=false,negocios revisados=1000, porcentaje entrenamiento= 30%, #usuarios prueba= 50, numero de recomendaciones x usuario= 100}";
        er.recall=0.003755288302;
        er.precision=0.0158;
        er.time=14383.33333;
        precalculatedEvaluations.add(er);

        //Colaborativo
        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 956703932, porcentaje entrenamiento= 50%, usuarios revisados=6040 similaridad: Euclidean}";
        er.recall=1;
        er.precision=0.16179;
        er.time=143205.473;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: {relevance=500, timestamp = 956703932, porcentaje entrenamiento= 30%, usuarios revisados:6040, similaridad: Pearson}";
        er.recall=1;
        er.precision=0.16179;
        er.time=143205.473;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 964473816, porcentaje entrenamiento= 50%, usuarios revisados=4922 , similaridad: Euclidean}";
        er.precision=0.1611;
        er.recall=1;
        er.time=142911.47087;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 964473816,porcentaje entrenamiento= 30%, usuarios revisados=4922 similaridad: Pearson}";
        er.precision=0.1611;
        er.recall=1;
        er.time=157202.61797;
        precalculatedEvaluations.add(er);
        
        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 972243701, porcentaje entrenamiento= 50%, usuarios revisados=3492 , similaridad: Euclidean}";
        er.precision=0.1422;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 972243701,porcentaje entrenamiento= 30%, usuarios revisados=3492 similaridad: Pearson}";
        er.precision=0.1422;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=500, timestamp = 1009349145, porcentaje entrenamiento= 50%, usuarios revisados=594 , similaridad: Euclidean}";
        er.precision=0.0105;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance = 500,timestamp = 1009349145,porcentaje entrenamiento= 30%, usuarios revisados=594 similaridad: Pearson}";
        er.precision=0.0105;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);
        //Relevancia 300
        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 956703932, porcentaje entrenamiento= 50%, usuarios revisados=6040 similaridad: Euclidean}";
        er.recall=1;
        er.precision=0.2871;
        er.time=143205.473;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: {relevance=300, timestamp = 956703932, porcentaje entrenamiento= 30%, usuarios revisados:6040, similaridad: Pearson}";
        er.recall=1;
        er.precision=0.2871;
        er.time=143205.473;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 964473816, porcentaje entrenamiento= 50%, usuarios revisados=4922 , similaridad: Euclidean}";
        er.precision=0.2821;
        er.recall=1;
        er.time=142911.47087;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 964473816,porcentaje entrenamiento= 30%, usuarios revisados=4922 similaridad: Pearson}";
        er.precision=0.2821;
        er.recall=1;
        er.time=142911.61797;
        precalculatedEvaluations.add(er);
        
        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 972243701, porcentaje entrenamiento= 50%, usuarios revisados=3492 , similaridad: Euclidean}";
        er.precision=0.2419;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 972243701,porcentaje entrenamiento= 30%, usuarios revisados=3492 similaridad: Pearson}";
        er.precision=0.2419;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 1009349145, porcentaje entrenamiento= 50%, usuarios revisados=594 , similaridad: Euclidean}";
        er.precision=0.0173;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);

        er=new EvaluationResult();
        er.description="<b>Recomendador colaborativo</b>: { relevance=300, timestamp = 1009349145,porcentaje entrenamiento= 30%, usuarios revisados=594 similaridad: Pearson}";
        er.precision=0.0173;
        er.recall=1;
        er.time=1432054.7;
        precalculatedEvaluations.add(er);


        return precalculatedEvaluations;
    }
}
