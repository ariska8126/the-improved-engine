/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author creative
 */
@Entity
@Table(name = "calendar_holiday")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarHoliday.findAll", query = "SELECT c FROM CalendarHoliday c"),
    @NamedQuery(name = "CalendarHoliday.findByCalendarHolidayId", query = "SELECT c FROM CalendarHoliday c WHERE c.calendarHolidayId = :calendarHolidayId"),
    @NamedQuery(name = "CalendarHoliday.findByCalendarHolidayDate", query = "SELECT c FROM CalendarHoliday c WHERE c.calendarHolidayDate = :calendarHolidayDate"),
    @NamedQuery(name = "CalendarHoliday.findByCalendarHolidayRemark", query = "SELECT c FROM CalendarHoliday c WHERE c.calendarHolidayRemark = :calendarHolidayRemark"),
    @NamedQuery(name = "CalendarHoliday.findByCalendarHolidayStatus", query = "SELECT c FROM CalendarHoliday c WHERE c.calendarHolidayStatus = :calendarHolidayStatus"),
    @NamedQuery(name = "CalendarHoliday.findByCalendarHolidayActive", query = "SELECT c FROM CalendarHoliday c WHERE c.calendarHolidayActive = :calendarHolidayActive")})
public class CalendarHoliday implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "calendar_holiday_id")
    private Integer calendarHolidayId;
    @Basic(optional = false)
    @Column(name = "calendar_holiday_date")
    @Temporal(TemporalType.DATE)
    private Date calendarHolidayDate;
    @Basic(optional = false)
    @Column(name = "calendar_holiday_remark")
    private String calendarHolidayRemark;
    @Basic(optional = false)
    @Column(name = "calendar_holiday_status")
    private String calendarHolidayStatus;
    @Basic(optional = false)
    @Column(name = "calendar_holiday_active")
    private String calendarHolidayActive;

    public CalendarHoliday() {
    }

    public CalendarHoliday(Integer calendarHolidayId) {
        this.calendarHolidayId = calendarHolidayId;
    }

    public CalendarHoliday(Integer calendarHolidayId, Date calendarHolidayDate, String calendarHolidayRemark, String calendarHolidayStatus, String calendarHolidayActive) {
        this.calendarHolidayId = calendarHolidayId;
        this.calendarHolidayDate = calendarHolidayDate;
        this.calendarHolidayRemark = calendarHolidayRemark;
        this.calendarHolidayStatus = calendarHolidayStatus;
        this.calendarHolidayActive = calendarHolidayActive;
    }

    public Integer getCalendarHolidayId() {
        return calendarHolidayId;
    }

    public void setCalendarHolidayId(Integer calendarHolidayId) {
        this.calendarHolidayId = calendarHolidayId;
    }

    public Date getCalendarHolidayDate() {
        return calendarHolidayDate;
    }

    public void setCalendarHolidayDate(Date calendarHolidayDate) {
        this.calendarHolidayDate = calendarHolidayDate;
    }

    public String getCalendarHolidayRemark() {
        return calendarHolidayRemark;
    }

    public void setCalendarHolidayRemark(String calendarHolidayRemark) {
        this.calendarHolidayRemark = calendarHolidayRemark;
    }

    public String getCalendarHolidayStatus() {
        return calendarHolidayStatus;
    }

    public void setCalendarHolidayStatus(String calendarHolidayStatus) {
        this.calendarHolidayStatus = calendarHolidayStatus;
    }

    public String getCalendarHolidayActive() {
        return calendarHolidayActive;
    }

    public void setCalendarHolidayActive(String calendarHolidayActive) {
        this.calendarHolidayActive = calendarHolidayActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarHolidayId != null ? calendarHolidayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalendarHoliday)) {
            return false;
        }
        CalendarHoliday other = (CalendarHoliday) object;
        if ((this.calendarHolidayId == null && other.calendarHolidayId != null) || (this.calendarHolidayId != null && !this.calendarHolidayId.equals(other.calendarHolidayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hadirapp.HadirAuth.entity.CalendarHoliday[ calendarHolidayId=" + calendarHolidayId + " ]";
    }
    
}
