package com.paulo.aws.test;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.costexplorer.CostExplorerClient;
import software.amazon.awssdk.services.costexplorer.model.DateInterval;
import software.amazon.awssdk.services.costexplorer.model.GetCostAndUsageRequest;
import software.amazon.awssdk.services.costexplorer.model.Granularity;
import software.amazon.awssdk.services.costexplorer.model.GroupDefinition;
import software.amazon.awssdk.services.organizations.OrganizationsClient;
import software.amazon.awssdk.services.organizations.model.ListTagsForResourceRequest;

@Component
public class ApplicationRunner implements CommandLineRunner {

	@Autowired
	private ClientFactory factory;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(" ApplicationRunner called");
		String startDate = "2020-03-01";
		String endDate = "2020-04-01";

		OrganizationsClient organizationsClient = factory.createOrganizationsClient();
		CostExplorerClient costExplorerClient = factory.createCostExplorerClient();

		organizationsClient.listAccounts().accounts().stream().forEach(account -> {
			System.out.println(account);
			ListTagsForResourceRequest listTagsForResourceRequest = ListTagsForResourceRequest.builder()
					.resourceId(account.id()).build();
			organizationsClient.listTagsForResource(listTagsForResourceRequest).tags().stream().forEach(tag -> {
				System.out.println(tag);

				GetCostAndUsageRequest getCostAndUsageRequest = GetCostAndUsageRequest.builder()
						.timePeriod(DateInterval.builder().start(startDate).end(endDate).build())
						.granularity(Granularity.MONTHLY).metrics(Arrays.asList("BlendedCost"))
						.groupBy(GroupDefinition.builder().type("DIMENSION").key("LINKED_ACCOUNT").build()).build();

				costExplorerClient.getCostAndUsage(getCostAndUsageRequest).resultsByTime().stream()
						.forEach(resultByTime -> {
							System.out.println(resultByTime);
							resultByTime.groups().stream().forEach(group -> {
								group.metrics().values().stream().forEach(metricValue -> {
									System.out.println(metricValue.amount());
								});
							});
						});
				;
			});
		});
	}

}