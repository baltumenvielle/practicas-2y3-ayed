package tp5.ejercicio3;

import tp5.ejercicio1.*;
import java.util.List;
import java.util.LinkedList;
import tp5.ejercicio1.adjList.*;

public class Mapa {
	
	private Graph<String> mapaCiudades;
	
	 public Mapa(Graph<String> mapa) {
	        this.setMapaCiudades(mapa);
	    }

	    public Graph<String> getMapaCiudades() {
	        return mapaCiudades;
	    }

	    public void setMapaCiudades(Graph<String> mapaCiudades) {
	        this.mapaCiudades = mapaCiudades;
	    }
	
	public List<String> devolverCamino(String ciudad1, String ciudad2) {
		List<String> camino = new LinkedList<String>();
		boolean[] visitados = new boolean[mapaCiudades.getSize()];
		
		if (!mapaCiudades.isEmpty()) {
			Vertex<String> origen = mapaCiudades.search(ciudad1);
			Vertex<String> destino = mapaCiudades.search(ciudad2);
			
			if (origen != null && destino != null) {
				caminoHelper(origen, visitados, camino, destino);
			}
		}
		return camino;
	}
	
	private boolean caminoHelper(Vertex<String> vertice, boolean[] visitados, List<String> camino, Vertex<String> destino) {
		visitados[vertice.getPosition()] = true;
		camino.add(vertice.getData());
		
		if (vertice == destino) return true;
		
		boolean encontre = false;
		List<Edge<String>> aristas = mapaCiudades.getEdges(vertice);
		for (Edge<String> arista: aristas) {
			int pos = arista.getTarget().getPosition();
			
			if (!visitados[pos] && !encontre) {
				encontre = caminoHelper(arista.getTarget(), visitados, camino, destino);
			}
		}
		if (!encontre) camino.remove(camino.size()-1);
		return encontre;
	}
	
	public List<String> devolverCaminoExceptuando(String ciudad1, String ciudad2, List<String> ciudades) {
		List<String> camino = new LinkedList<String>();
		boolean[] visitados = new boolean[mapaCiudades.getSize()];
		
		if (!mapaCiudades.isEmpty()) {
			Vertex<String> origen = mapaCiudades.search(ciudad1);
			Vertex<String> destino = mapaCiudades.search(ciudad2);
			
			if (origen != null  && destino != null) {
				caminoExceptuandoHelper(origen, visitados, ciudades, camino, destino);
			}
		}
		return camino;
	}
	
	private boolean caminoExceptuandoHelper(Vertex<String> vertice, boolean[] visitados, List<String> ciudades, List<String> camino, Vertex<String> destino) {
		visitados[vertice.getPosition()] = true;
		camino.add(vertice.getData());
		
		if (vertice == destino) return true;
		
		boolean encontre = false;
		List<Edge<String>> aristas = mapaCiudades.getEdges(vertice);
		for (Edge<String> arista: aristas) {
			int pos = arista.getTarget().getPosition();
			String ciudad = arista.getTarget().getData();
			
			if (ciudades.contains(ciudad)) visitados[arista.getTarget().getPosition()] = true;
			
			if (!visitados[pos] && !encontre) {
				encontre = caminoExceptuandoHelper(arista.getTarget(), visitados, ciudades, camino, destino);
			}
		}
		if (!encontre) {
			camino.remove(camino.size()-1);
			visitados[vertice.getPosition()] = false;
		}
		return encontre;
	}
	
	public List<String> caminoMasCorto(String ciudad1, String ciudad2) {
		List<String> caminoActual = new LinkedList<String>(), caminoMinimo = new LinkedList<String>();
		boolean[] visitados = new boolean[mapaCiudades.getSize()];
		
		if (!mapaCiudades.isEmpty()) {
			Vertex<String> origen = mapaCiudades.search(ciudad1);
			Vertex<String> destino = mapaCiudades.search(ciudad2);
			
			if (origen != null && destino != null) {
				caminoCortoHelper(origen, visitados, caminoActual, caminoMinimo, destino, 0, Integer.MAX_VALUE);
			}
		}
		
		return caminoMinimo;
	}
	
	private int caminoCortoHelper(Vertex<String> vertice, boolean[] visitados, List<String> caminoActual, List<String> caminoMinimo, Vertex<String> destino, int actual, int min) {
		visitados[vertice.getPosition()] = true;
		caminoActual.add(vertice.getData());
		
		if (vertice == destino && actual < min) {
			caminoMinimo.removeAll(caminoMinimo);
			caminoMinimo.addAll(caminoActual);
			min = actual;
		}
		
		List<Edge<String>> aristas = mapaCiudades.getEdges(vertice);
		for (Edge<String> arista: aristas) {
			int pos = arista.getTarget().getPosition();
			int peso = actual + arista.getWeight();
			
			if (!visitados[pos] && peso < min) {
				min  = caminoCortoHelper(arista.getTarget(), visitados, caminoActual, caminoMinimo, destino, peso, min);
			}
		}
		caminoActual.remove(caminoActual.size()-1);
		visitados[vertice.getPosition()] = false;
		return min;
	}
	
	public List<String> caminoSinCargarCombustible(String ciudad1, String ciudad2, int tanqueAuto) {
		List<String> camino = new LinkedList<String>();
		boolean[] visitados = new boolean[mapaCiudades.getSize()];
		
		if (!mapaCiudades.isEmpty()) {
			Vertex<String> origen = mapaCiudades.search(ciudad1);
			Vertex<String> destino = mapaCiudades.search(ciudad2);
			
			if (origen != null && destino != null) {
				caminoSinCargarHelper(origen, visitados, camino, tanqueAuto, destino);
			}
		}
		return camino;
	}
	
	private boolean caminoSinCargarHelper(Vertex<String> vertice, boolean[] visitados, List<String> camino, int tanque, Vertex<String> destino) {
		visitados[vertice.getPosition()] = true;
		camino.add(vertice.getData());
		
		if (vertice == destino) {
			return true;
		}
		
		boolean encontre = false;
		List<Edge<String>> aristas = mapaCiudades.getEdges(vertice);
		for (Edge<String> arista: aristas) {
			int pos = arista.getTarget().getPosition();
			int distancia = arista.getWeight();
			
			if (!visitados[pos] && tanque-distancia >= 0 && !encontre) {
				encontre = caminoSinCargarHelper(arista.getTarget(), visitados, camino, (tanque-distancia), destino);
			}
		}
		if (!encontre) camino.remove(camino.size()-1);
		visitados[vertice.getPosition()] = false;
		return encontre;
	}
	
	public List<String> caminoConMenorCargaDeCombustible(String ciudad1, String ciudad2, int tanqueAuto) {
		List<String> caminoActual = new LinkedList<String>(), caminoMinimo = new LinkedList<String>();
		boolean[] visitados = new boolean[mapaCiudades.getSize()];
		
		if (!mapaCiudades.isEmpty()) {
			Vertex<String> origen = mapaCiudades.search(ciudad1);
			Vertex<String> destino = mapaCiudades.search(ciudad2);
			
			if (origen != null && destino != null) {
				caminoConMenorCargaHelper(origen, visitados, caminoActual, caminoMinimo, tanqueAuto, tanqueAuto, 0, Integer.MAX_VALUE, destino);
			}
		}
		return caminoMinimo;
	}
	
	private int caminoConMenorCargaHelper(Vertex<String> vertice, boolean[] visitados, List<String> caminoActual, List<String> caminoMinimo, int tanqueActual, int tanque, int recargas, int minRecargas, Vertex<String> destino) {
		visitados[vertice.getPosition()] = true;
		caminoActual.add(vertice.getData());
		
		if (vertice == destino && recargas < minRecargas) {
			caminoMinimo.removeAll(caminoMinimo);
			caminoMinimo.addAll(caminoActual);
			minRecargas = recargas;
		}
		
		List<Edge<String>> aristas = mapaCiudades.getEdges(vertice);
		for (Edge<String> arista: aristas) {
			int pos = arista.getTarget().getPosition();
			int distancia = arista.getWeight();
			
			if (!visitados[pos]) {
				if (tanqueActual >= distancia) {
					minRecargas = caminoConMenorCargaHelper(arista.getTarget(), visitados, caminoActual, caminoMinimo, (tanqueActual - distancia), tanque, recargas, minRecargas, destino);
				}
				else if (tanque >= distancia) {
					minRecargas = caminoConMenorCargaHelper(arista.getTarget(), visitados, caminoActual, caminoMinimo, (tanque - distancia), tanque, recargas+1, minRecargas, destino);
				}
			}
		}
		visitados[vertice.getPosition()] = false;
		caminoActual.remove(caminoActual.size()-1);
		return minRecargas;
	}

	public static void main(String[] args) {
		Graph<String> mapaCiudades = new AdjListGraph<String>();
        Vertex<String> v1 = mapaCiudades.createVertex("La Plata"); //Casa Caperucita
        Vertex<String> v2 = mapaCiudades.createVertex("Ensenada"); //Claro 3
        Vertex<String> v3 = mapaCiudades.createVertex("Berisso"); //Claro 1
        Vertex<String> v4 = mapaCiudades.createVertex("Berazategui"); //Claro 2
        Vertex<String> v5 = mapaCiudades.createVertex("Florencio Varela"); //Claro 5
        Vertex<String> v6 = mapaCiudades.createVertex("Presidente Peron"); //Claro 4
        Vertex<String> v7 = mapaCiudades.createVertex("San Vicente"); //Casa Abuelita
        mapaCiudades.connect(v1, v2, 4);
        mapaCiudades.connect(v2, v1, 4);
        mapaCiudades.connect(v1, v3, 3);
        mapaCiudades.connect(v3, v1, 3);
        mapaCiudades.connect(v1, v4, 4);
        mapaCiudades.connect(v4, v1, 4);
        mapaCiudades.connect(v2, v5, 15);
        mapaCiudades.connect(v5, v2, 15);
        mapaCiudades.connect(v3, v5, 3);
        mapaCiudades.connect(v5, v3, 3);
        mapaCiudades.connect(v4, v3, 4);
        mapaCiudades.connect(v3, v4, 4);
        mapaCiudades.connect(v4, v5, 11);
        mapaCiudades.connect(v5, v4, 11);
        mapaCiudades.connect(v4, v6, 10);
        mapaCiudades.connect(v6, v4, 10);
        mapaCiudades.connect(v4, v3, 4);
        mapaCiudades.connect(v3, v4, 4);
        mapaCiudades.connect(v5, v7, 4);
        mapaCiudades.connect(v7, v5, 4);
        mapaCiudades.connect(v6, v7, 9);
        mapaCiudades.connect(v7, v6, 9);
        
        Mapa mapa = new Mapa(mapaCiudades);
        
        System.out.print("LISTA DEVOLVER CAMINO: ");
        System.out.print(mapa.devolverCamino("La Plata", "San Vicente"));
        
        System.out.println("");
        
        System.out.print("LISTA DEVOLVER CAMINO EXCEPTUANDO LUGARES:");
        List<String> restringidos = new LinkedList<String>();
        restringidos.add("Berisso");
        System.out.print(mapa.devolverCaminoExceptuando("La Plata", "San Vicente", restringidos));
        
        System.out.println("");
        
        System.out.print("LISTA CAMINO MAS CORTO (SEGUN DISTANCIA): ");
        System.out.print(mapa.caminoMasCorto("La Plata", "San Vicente"));
        
        System.out.println("");
        
        System.out.print("LISTA CAMINO SIN CARGAR COMBUSTIBLE: ");
        System.out.print(mapa.caminoSinCargarCombustible("La Plata", "San Vicente", 30));
        
        System.out.println("");
        
        System.out.print("LISTA CAMINO CON MENOR CARGAS DE COMBUSTIBLE: ");
        System.out.print(mapa.caminoConMenorCargaDeCombustible("La Plata", "San Vicente", 10));
        
        System.out.println("");
	}

}
