package com.ktrental.model;

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

	private int subscription = 0;
	private int subscriptionWaitting = 0;
	private int transfer = 0;
	private int complate = 0;
	private int plan = 0;
	private int possetion = 0;
	private int progress_s = 0;
	private int emergency = 0;

	public int getEmergency() {
		return emergency;
	}

	public void setEmergency(int emergency) {
		this.emergency = emergency;
	}

	public RepairPlanModel() {

		// plan = subscription + subscriptionWaitting + transfer + complate;
	}

	public void addWork(String workName, boolean planFlag) {
		for (int i = 0; i < workArr.length; i++) {
			if (workArr[i].equals(workName)) {
				if (i == 0) {
					subscriptionWaitting++;

					plan++;

					break;
				} else if (i == 1) {
					subscription++;
					plan++;
					break;
				} else if (i == 2) {
					transfer++;
					plan++;
					break;
				} else if (i == 3) {

					complate++;

					if (planFlag)
						emergency++;
					else
						plan++;
					break;
				} else if (i == 4) {
					possetion++;
					plan++;
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
