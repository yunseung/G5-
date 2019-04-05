package com.ktrental.model;

import android.util.Log;

public class RepairPlanModel {
	public final static String ALL = "ALL_REPAIR"; // 예약대기
	public final static String E0001 = "E0001"; // 예약대기
	public final static String E0002 = "E0002"; // 예약
	public final static String E0003 = "E0003"; // 이관 요청
	public final static String E0004 = "E0004"; // 완료
	public final static String E0005 = "E0005"; // 사유등록

	public final static String E0001_VALUE = "예약대기"; // 예약대기
	public final static String E0002_VALUE = "예약"; // 예약
	public final static String E0003_VALUE = "이관요청"; // 이관 요청
	public final static String E0004_VALUE = "완료"; // 완료
	public final static String E0005_VALUE = "사유등록"; // 사유등록

	private String[] workArr = { E0001, E0002, E0003, E0004, E0005 };

	private int subscription, subscription2, subscription3 = 0;
	private int subscriptionWaitting, subscriptionWaitting2, subscriptionWaitting3 = 0;
	private int transfer, transfer2, transfer3 = 0;
	private int complate, complate2, complate3 = 0;
	private int plan, plan2, plan3 = 0;
	private int possetion, possetion2, possetion3 = 0;
	private int progress_s = 0;
	private int emergency, emergency2, emergency3 = 0;

	private String mGubun;

	public int getEmergency() {
		return emergency;
	}

	public String getGubun() {
		return mGubun;
	}

	public void setEmergency(int emergency) {
		this.emergency = emergency;
	}

	public RepairPlanModel() {

		// plan = subscription + subscriptionWaitting + transfer + complate;
	}

	public void addWork(String workName, boolean planFlag, String gubun) {
		mGubun = gubun;
		for (int i = 0; i < workArr.length; i++) {
			if (workArr[i].equals(workName)) {
				if (i == 0) {
					if (gubun.trim().isEmpty()) {
						subscriptionWaitting++;

						plan++;

					} else if (gubun.trim().equals("A")) {
						subscriptionWaitting2++;

						plan2++;

					} else if (gubun.trim().equals("O")) {
						subscriptionWaitting3++;

						plan3++;
					}


					break;
				} else if (i == 1) {
					if (gubun.trim().isEmpty()) {
						subscription++;
						plan++;
					} else if (gubun.trim().equals("A")) {
						subscription2++;
						plan2++;
					} else if (gubun.trim().equals("O")) {
						subscription3++;
						plan3++;
					}

					break;
				} else if (i == 2) {
					if (gubun.trim().isEmpty()) {
						transfer++;
						plan++;
					} else if (gubun.trim().equals("A")) {
						transfer2++;
						plan2++;
					} else if (gubun.trim().equals("O")) {
						transfer3++;
						plan3++;
					}

					break;
				} else if (i == 3) {
					if (gubun.trim().isEmpty()) {
						complate++;
					} else if (gubun.trim().equals("A")) {
						complate2++;
					} else if (gubun.trim().equals("O")) {
						complate3++;
					}


					if (planFlag) {
						if (gubun.trim().isEmpty()) {
							emergency++;
						} else if (gubun.trim().equals("A")) {
							emergency2++;
						} else if (gubun.trim().equals("O")) {
							emergency3++;
						}

					} else {
						if (gubun.trim().isEmpty()) {
							plan++;
						} else if (gubun.trim().equals("A")) {
							plan2++;
						} else if (gubun.trim().equals("O")) {
							plan3++;
						}
					}

					break;
				} else if (i == 4) {
					if (gubun.trim().isEmpty()) {
						possetion++;
						plan++;
					} else if (gubun.trim().equals("A")) {
						possetion2++;
						plan2++;
					} else if (gubun.trim().equals("O")) {
						possetion3++;
						plan3++;
					}

					break;
				}
			}
		}
	}

	public int getSubscription() {
		return subscription;
	}

	public void setSubscription(int subscription) {
		this.subscription = subscription;
	}

	public int getSubscriptionWaitting() {
		return subscriptionWaitting;
	}

	public void setSubscriptionWaitting(int subscriptionWaitting) {
		this.subscriptionWaitting = subscriptionWaitting;
	}
	
	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

	public int getComplate() {
		return complate;
	}

	public int getTotalComplateForDayInfo() {
		return complate + complate2 + complate3;
	}

	public int getTotalPlanCountForDayInfo() {
		return plan + plan2 + plan3;
	}

	public void setComplate(int complate) {
		this.complate = complate;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getPossetion() {
		return possetion;
	}

	public void setPossetion(int possetion) {
		this.possetion = possetion;
	}

	public int getSubscription2() {
		return subscription2;
	}

	public int getSubscription3() {
		return subscription3;
	}

	public int getSubscriptionWaitting2() {
		return subscriptionWaitting2;
	}

	public int getSubscriptionWaitting3() {
		return subscriptionWaitting3;
	}

	public int getTransfer2() {
		return transfer2;
	}

	public int getTransfer3() {
		return transfer3;
	}

	public int getComplate2() {
		return complate2;
	}

	public int getComplate3() {
		return complate3;
	}

	public int getPlan2() {
		return plan2;
	}

	public int getPlan3() {
		return plan3;
	}

	public int getPossetion2() {
		return possetion2;
	}

	public int getPossetion3() {
		return possetion3;
	}

	public int getEmergency2() {
		return emergency2;
	}

	public int getEmergency3() {
		return emergency3;
	}

	public static String getProgressStatus(String aProgressStatus) {
		String reProgressStatus = aProgressStatus;

		if (aProgressStatus != null) {
			if (aProgressStatus.equals(E0001)) {
				reProgressStatus = E0001_VALUE;
			} else if (aProgressStatus.equals(E0002)) {
				reProgressStatus = E0002_VALUE;
			} else if (aProgressStatus.equals(E0003)) {
				reProgressStatus = E0003_VALUE;
			} else if (aProgressStatus.equals(E0004)) {
				reProgressStatus = E0004_VALUE;
			} else if (aProgressStatus.equals(E0005)) {
				reProgressStatus = E0005_VALUE;
			}
		}

		return reProgressStatus;
	}

	public static String getPeriodStatus(String aPeriod) {
		String rePreiod = aPeriod;

		if (rePreiod != null) {
			if (rePreiod.equals("1")) {
				rePreiod = "장기";
			} else if (rePreiod.equals("2")) {
				rePreiod = "중고차장기";
			} else if (rePreiod.equals("3")) {
				rePreiod = "월장기";
			} else if (rePreiod.equals("4")) {
				rePreiod = "단기";
			}
		}

		return rePreiod;
	}

}
