package com.professionalit.client;

import org.springframework.stereotype.Service;

import com.professionalit.grpc.WalletRequest;
import com.professionalit.grpc.WalletResponse;
import com.professionalit.grpc.WalletServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class WalletGrpcClient {

	@GrpcClient("wallet-service")
	private WalletServiceGrpc.WalletServiceBlockingStub walletStub;

	public boolean validateBalance(String userId, double amount) {

		WalletRequest request = WalletRequest.newBuilder().setUserId(userId).setAmount(amount).build();

		WalletResponse response = walletStub.validateWallet(request);

		return response.getValid();
	}

}