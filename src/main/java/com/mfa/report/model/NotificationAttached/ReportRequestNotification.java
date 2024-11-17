package com.mfa.report.model.NotificationAttached;

import com.mfa.report.model.Direction;
import com.mfa.report.model.Notification;
import com.mfa.report.model.ReportRequest;
import com.mfa.report.model.User;
import com.mfa.report.model.enumerated.NotificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_REPORT_CREATED")
public class ReportRequestNotification extends Notification {

    @ManyToOne(cascade = CascadeType.ALL)
    private ReportRequest reportRequest;


    public ReportRequestNotification() {
    }
    public  ReportRequestNotification (ReportRequest request, User recipient){
        super.setDescription("[DEMANDE DE RAPPORT] - "+request.getRequestingDirection().getAcronym()+" "+request.getDescription());
        super.setUser(recipient);
        super.setCreationDatetime(LocalDateTime.now());
        super.setNotificationType(NotificationStatus.report_demand);
        this.reportRequest = request;
    }

    public  String getRequestReportId(String id){
        return  reportRequest.getId();
    }
}
