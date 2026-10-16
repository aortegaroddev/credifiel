import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Persona, PersonaPayload } from '../models/persona.model';

const PERSONAS_API_URL = 'http://localhost:8080/api/personas';

@Injectable({ providedIn: 'root' })
export class PersonaApiService {
  private readonly http = inject(HttpClient);

  list(): Observable<Persona[]> {
    return this.http.get<Persona[]>(PERSONAS_API_URL);
  }

  getById(id: number): Observable<Persona> {
    return this.http.get<Persona>(`${PERSONAS_API_URL}/${id}`);
  }

  create(payload: PersonaPayload): Observable<Persona> {
    return this.http.post<Persona>(PERSONAS_API_URL, payload);
  }

  update(id: number, payload: PersonaPayload): Observable<Persona> {
    return this.http.put<Persona>(`${PERSONAS_API_URL}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${PERSONAS_API_URL}/${id}`);
  }
}
