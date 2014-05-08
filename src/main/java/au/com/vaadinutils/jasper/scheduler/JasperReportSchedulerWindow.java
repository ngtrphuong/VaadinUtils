package au.com.vaadinutils.jasper.scheduler;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import au.com.vaadinutils.jasper.parameter.ReportParameter;
import au.com.vaadinutils.jasper.scheduler.entities.DateParameterOffsetType;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailParameterEntity;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailScheduleEntity;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailScheduledDateParameter;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailSender;
import au.com.vaadinutils.jasper.scheduler.entities.ScheduleMode;
import au.com.vaadinutils.jasper.ui.JasperReportProperties;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class JasperReportSchedulerWindow extends Window
{
	private static final long serialVersionUID = 1L;

	public JasperReportSchedulerWindow(final JasperReportProperties reportProperties,
			final Collection<ReportParameter<?>> params)
	{

		this.setWidth("90%");
		this.setHeight("80%");

		this.setContent(new JasperReportScheduleLayout(new ScheduleCreater()
		{

			@Override
			public ReportEmailScheduleEntity create()
			{
				ReportEmailScheduleEntity schedule = new ReportEmailScheduleEntity();
				schedule.setTitle(reportProperties.getReportTitle());
				schedule.setReportFilename(reportProperties.getReportFileName());
				schedule.setMessage(reportProperties.getReportTitle() + " report is attached");
				schedule.setSubject(reportProperties.getReportTitle());
				schedule.setReportClass(reportProperties.getReportClass());
				schedule.setScheduleMode(ScheduleMode.ONE_TIME);
				schedule.setOneTimeRunTime(new Date());
				schedule.setEnabled(false);

				List<ReportEmailParameterEntity> rparams = new LinkedList<ReportEmailParameterEntity>();
				List<ReportEmailScheduledDateParameter> dparams = new LinkedList<ReportEmailScheduledDateParameter>();
				for (ReportParameter<?> param : params)
				{
					if (param.isDateField())
					{
						// add date fields
						ReportEmailScheduledDateParameter rparam = new ReportEmailScheduledDateParameter();

						String[] names = param.getParameterNames().toArray(new String[] {});
						rparam.setStartName(names[0]);
						rparam.setStartDate(param.getStartDate());
						rparam.setEndName(names[1]);
						rparam.setEndDate(param.getEndDate());

						rparam.setType(param.getDateParameterType());
						rparam.setOffsetType(DateParameterOffsetType.TODAY);
						rparam.setLabel(param.getLabel());
						dparams.add(rparam);

					}

				}
				schedule.setParameters(rparams);

				schedule.setDateParameters(dparams);

				ReportEmailSender reportEmailSender = new ReportEmailSender();
				reportEmailSender.setUserName(reportProperties.getUsername());
				reportEmailSender.setEmailAddress(reportProperties.getUserEmailAddress());
				schedule.setSender(reportEmailSender);

				return schedule;
			}
		}));
		setModal(true);
		// center();
		UI.getCurrent().addWindow(this);

	}
}
