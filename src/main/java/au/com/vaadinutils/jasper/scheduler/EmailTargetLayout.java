package au.com.vaadinutils.jasper.scheduler;

import java.util.LinkedList;
import java.util.List;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailRecipient;
import au.com.vaadinutils.jasper.scheduler.entities.ReportEmailRecipientVisibility;
import au.com.vaadinutils.validator.EmailValidator;

import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractInMemoryContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;

public class EmailTargetLayout extends VerticalLayout
{
	// Logger logger = LogManager.getLogger();
	List<TargetLine> lines = new LinkedList<TargetLine>();

	EmailTargetLayout()
	{
		setSizeFull();
		setSpacing(true);
		setMargin(new MarginInfo(true, true, false, true));

	}

	private TargetLine insertTargetLine(final int row, ReportEmailRecipient recip)
	{

		final HorizontalLayout recipientHolder = new HorizontalLayout();
		recipientHolder.setSizeFull();
		recipientHolder.setSpacing(true);

		final List<ReportEmailRecipientVisibility> targetTypes = new LinkedList<ReportEmailRecipientVisibility>();
		for (ReportEmailRecipientVisibility rerv : ReportEmailRecipientVisibility.values())
		{
			targetTypes.add(rerv);
		}

		final TargetLine line = new TargetLine();
		line.row = row;

		line.targetTypeCombo = new ComboBox(null, targetTypes);
		line.targetTypeCombo.setWidth("60");
		line.targetTypeCombo.select(targetTypes.get(0));

		line.targetAddress = new ComboBox(null);
		line.targetAddress.setImmediate(true);
		line.targetAddress.setTextInputAllowed(true);
		line.targetAddress.setInputPrompt("Enter Contact Name or email address");
		line.targetAddress.setWidth("100%");
		line.targetAddress.addValidator(new EmailValidator("Please enter a valid email address."));

		line.targetAddress.setContainerDataSource(getValidEmailContacts());
		line.targetAddress.setItemCaptionPropertyId("namedemail");
		line.targetAddress.setNewItemsAllowed(true);
		if (recip != null && recip.getEmail() != null)
		{
			line.targetAddress.setValue(recip.getEmail());
		}

		line.targetAddress.setNewItemHandler(new NewItemHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void addNewItem(final String newItemCaption)
			{
				final IndexedContainer container = (IndexedContainer) line.targetAddress.getContainerDataSource();

				final Item item = addItem(container, "", newItemCaption);
				if (item != null)
				{
					line.targetAddress.addItem(item.getItemProperty("id").getValue());
					line.targetAddress.setValue(item.getItemProperty("id").getValue());
				}
			}
		});

		if (recip != null)
		{

		}

		if (row == 0)
		{
			line.actionButton = new Button("+");
			line.actionButton.setDescription("Click to add another email address line.");
			line.actionButton.setStyleName(Reindeer.BUTTON_SMALL);
			line.actionButton.addClickListener(new ClickListener()
			{

				private static final long serialVersionUID = 6505218353927273720L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					lines.add(insertTargetLine(lines.size(), null));
				}
			});
		}
		else
		{
			line.actionButton = new Button("-");
			line.actionButton.setDescription("Click to remove this email address line.");
			line.actionButton.setStyleName(Reindeer.BUTTON_SMALL);
			line.actionButton.addClickListener(new ClickListener()
			{

				private static final long serialVersionUID = 3104323607502279386L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					removeComponent(recipientHolder);
					lines.remove(line);

				}
			});
		}

		recipientHolder.addComponent(line.targetTypeCombo);
		recipientHolder.addComponent(line.targetAddress);
		recipientHolder.addComponent(line.actionButton);
		recipientHolder.setExpandRatio(line.targetAddress, 1);

		addComponent(recipientHolder);

		return line;
	}

	private IndexedContainer getValidEmailContacts()
	{
		final IndexedContainer container = new IndexedContainer();

		JpaBaseDao<ReportEmailRecipient, Long> reportEmailRecipient = getGenericDao(ReportEmailRecipient.class);

		container.addContainerProperty("id", String.class, null);
		container.addContainerProperty("email", String.class, null);
		container.addContainerProperty("namedemail", String.class, null);

		for (final ReportEmailRecipient contact : reportEmailRecipient.findAll())
		{
			if (contact.getEmail() != null)
			{
				addItem(container, null, contact.getEmail());
			}
		}
		return container;
	}

	@SuppressWarnings("unchecked")
	private Item addItem(final IndexedContainer container, final String named, String email)
	{
		// When we are editing an email (as second time) we can end up with
		// double brackets so we strip them off here.
		if (email.startsWith("<"))
		{
			email = email.substring(1);
		}
		if (email.endsWith(">"))
		{
			email = email.substring(0, email.length() - 1);
		}

		final Item item = container.addItem(email);
		if (item != null)
		{
			item.getItemProperty("id").setValue(email);
			item.getItemProperty("email").setValue(email);
			String namedEmail;
			if (named != null && named.trim().length() > 0)
			{
				namedEmail = named + " <" + email + ">";
			}
			else
			{
				namedEmail = "<" + email + ">";
			}
			item.getItemProperty("namedemail").setValue(namedEmail);
		}
		return item;
	}

	private <E> JpaBaseDao<E, Long> getGenericDao(Class<E> class1)
	{
		return new JpaBaseDao<E, Long>(class1);

	}

	public List<TargetLine> getTargets()
	{
		return lines;
	}

	public void add(ReportEmailRecipient target)
	{
		lines.add(insertTargetLine(lines.size(), target));
		
	}

	public void clear()
	{
		lines.clear();
		removeAllComponents();
		
	}

}
