package com.paulo.aws.test;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.costexplorer.CostExplorerClient;
import software.amazon.awssdk.services.organizations.OrganizationsClient;

@Component
public class ClientFactory {

    public OrganizationsClient createOrganizationsClient() {
        return OrganizationsClient.builder().credentialsProvider(DefaultCredentialsProvider.create()).build();
    }

    public CostExplorerClient createCostExplorerClient() {
        return CostExplorerClient.builder().credentialsProvider(DefaultCredentialsProvider.create()).build();
    }
}