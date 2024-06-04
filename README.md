# -t-n-threshold-scheme
Project on Implementation and mitigation of insider threat in  Cyber-Physical Systems

# What is (t,n) Threshold Scheme?

A (t,n) threshold scheme is a cryptographic method used to enhance the security and reliability of secret sharing. This scheme allows a secret to be divided into 𝑛 shares, distributed to n participants, such that any 𝑡 or more shares can be used to reconstruct the original secret. The primary advantage of this scheme is that it does not require all participants to be present to reconstruct the secret, thereby improving fault tolerance and security.
The scheme was originally proposed by Adi Shamir and George Blakley independently in 1979. The Shamir's Secret Sharing Scheme is based on polynomial interpolation, while Blakley's scheme uses geometric principles.

# Example of (t,n) Threshold Scheme

Consider a scenario where a company wants to secure a secret key used for encrypting its sensitive data. The company employs a (3,5) threshold scheme. This means the secret key is divided into 5 shares, and any 3 of these shares are sufficient to reconstruct the original key.
Here is a step-by-step example using Shamir's Secret Sharing Scheme:
Secret Selection: The secret key 𝑆  is chosen.
Polynomial Construction: A random polynomial 𝑃(𝑥) of degree t−1 is constructed such that 𝑃(0)=𝑆. For instance, if 𝑆=1234 and t=3, a polynomial 𝑃(𝑥)=1234+2𝑥+3𝑥2 could be constructed.
Share Generation: Evaluate the polynomial at 𝑛 distinct points to generate shares. For example, P(1),P(2),P(3),P(4), and 𝑃(5) are calculated.
Distribution: Distribute the shares (1,P(1)),(2,P(2)),(3,P(3)),(4,P(4)),(5,P(5)) to 5 participants.
Reconstruction: To reconstruct the secret, any 3 participants combine their shares using Lagrange interpolation to solve for 𝑃(0) , which gives the secret 𝑆.

# Advantages

•	Enhanced Security: Threshold schemes enhance security by distributing the secret among multiple parties. Even if some of the shares are compromised or lost, the secret remains secure as long as the threshold requirement is not breached.
•	Resilience against Compromise: In a t,n threshold scheme, as long as fewer than t shares are compromised, the secret remains secure. This resilience against individual share compromise is crucial in scenarios where security breaches are possible.
•	Flexibility: Threshold schemes offer flexibility in configuring the level of security and accessibility. By adjusting the parameters t and n, organizations can tailor the scheme according to their specific security requirements and risk tolerance.
•	Key Management: With threshold cryptography, key management becomes more manageable, particularly in scenarios where multiple entities or individuals need access to sensitive information. The distribution of shares simplifies the key management process compared to traditional cryptographic methods.
•	Fault Tolerance: Threshold schemes provide fault tolerance against hardware failures or network outages. Even if some of the participating entities become unavailable, the system can still operate as long as the threshold requirement is met.
•	Reduced Trust Requirements: Threshold cryptography reduces the trust requirement on any single entity. Since the secret is distributed among multiple parties, no single entity holds complete control over the secret, thereby minimizing the risk of insider attacks or collusion.
•	Scalability: These schemes are scalable, allowing for the addition or removal of participants without requiring a complete overhaul of the system. This scalability is particularly beneficial in dynamic environments where the composition of participating entities may change over time.
•	Privacy Preservation: Threshold schemes can be designed to preserve privacy by ensuring that no single entity gains complete knowledge of the secret without collaboration with other parties. This property is valuable in applications where preserving privacy is paramount.

# Disadvantages

While the (t,n) threshold scheme offers significant advantages, it also has some disadvantages:
1.	Complexity: Implementing the scheme requires computational resources, especially for polynomial evaluation and interpolation.
2.	Overhead: The need to manage multiple shares and ensure their secure distribution can introduce administrative overhead.
3.	Risk of Share Compromise: If an attacker gains access to 𝑡 or more shares, they can reconstruct the secret. Thus, the security of each share is critical.
4.	Data Synchronization: Ensuring that shares are updated and synchronized in a dynamic environment where participants may change can be challenging.
