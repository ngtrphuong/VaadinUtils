/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package au.com.vaadinutils.jasper.scheduler.entities;

import java.lang.Long;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=au.com.vaadinutils.jasper.scheduler.entities.ReportEmailScheduledDateParameter.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Mon May 12 18:22:45 EST 2014")
public class ReportEmailScheduledDateParameter_ {
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,Date> endDate;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,String> endName;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,Long> iID;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,String> label;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,DateParameterOffsetType> offsetType;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,Date> startDate;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,String> startName;
    public static volatile SingularAttribute<ReportEmailScheduledDateParameter,DateParameterType> type;
}
