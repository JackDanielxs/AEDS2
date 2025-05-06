import pandas as pd
import matplotlib.pyplot as plt

#Carregar os dados do arquivo CSV
data = pd.read_csv("dados_ordenacao.csv")

#Gráfico de tempo de execução
plt.figure(figsize=(10, 6))
for Algoritmo in data["Algoritmo"].unique():
    subset = data[data["Algoritmo"] == Algoritmo]
    plt.plot(subset["Tamanho"], subset["Tempo"], label=Algoritmo, marker='o')
plt.title("Tempo de Execução por Algoritmo")
plt.xlabel("Tamanho do Vetor")
plt.ylabel("Tempo (ms)")
plt.legend()
plt.grid()
plt.savefig("tempo.png")
plt.close()

#Gráfico de número de comparações
plt.figure(figsize=(10, 6))
for Algoritmo in data["Algoritmo"].unique():
    subset = data[data["Algoritmo"] == Algoritmo]
    plt.plot(subset["Tamanho"], subset["Comparacoes"], label=Algoritmo, marker='o')
plt.title("Número de Comparações por Algoritmo")
plt.xlabel("Tamanho do Vetor")
plt.ylabel("Número de Comparações")
plt.legend()
plt.grid()
plt.savefig("comparacoes.png")
plt.close()

#Gráfico de número de movimentações
plt.figure(figsize=(10, 6))
for Algoritmo in data["Algoritmo"].unique():
    subset = data[data["Algoritmo"] == Algoritmo]
    plt.plot(subset["Tamanho"], subset["Movimentacoes"], label=Algoritmo, marker='o')
plt.title("Número de Movimentações por Algoritmo")
plt.xlabel("Tamanho do Vetor")
plt.ylabel("Número de Movimentações")
plt.legend()
plt.grid()
plt.savefig("movimentacoes.png")
plt.close()