import { ChangeDetectionStrategy, Component, OnInit, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import { PersonaApiService } from './core/api/persona-api.service';
import { Persona, PersonaPayload } from './core/models/persona.model';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnInit {
  private readonly personaApi = inject(PersonaApiService);
  private readonly formBuilder = inject(FormBuilder);

  readonly personas = signal<Persona[]>([]);
  readonly isLoading = signal(true);
  readonly isSaving = signal(false);
  readonly deletingId = signal<number | null>(null);
  readonly error = signal<string | null>(null);
  readonly success = signal<string | null>(null);
  readonly editingPersona = signal<Persona | null>(null);
  readonly detailPersona = signal<Persona | null>(null);
  readonly pendingDeletion = signal<Persona | null>(null);
  readonly isEditing = computed(() => this.editingPersona() !== null);

  readonly personaForm = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    edad: [0, [Validators.required, Validators.min(0), Validators.max(150)]],
  });

  ngOnInit(): void {
    this.loadPersonas();
  }

  loadPersonas(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.personaApi
      .list()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (personas) => this.personas.set(personas),
        error: () => this.error.set('No fue posible cargar las personas. Verifica que el servicio esté disponible.'),
      });
  }

  submit(): void {
    if (this.personaForm.invalid) {
      this.personaForm.markAllAsTouched();
      return;
    }

    const payload: PersonaPayload = this.personaForm.getRawValue();
    const persona = this.editingPersona();
    this.isSaving.set(true);
    this.error.set(null);

    const request = persona
      ? this.personaApi.update(persona.id, payload)
      : this.personaApi.create(payload);

    request.pipe(finalize(() => this.isSaving.set(false))).subscribe({
      next: (savedPersona) => {
        this.personas.update((personas) =>
          persona
            ? personas.map((current) => (current.id === savedPersona.id ? savedPersona : current))
            : [...personas, savedPersona],
        );
        this.success.set(persona ? 'La persona fue actualizada.' : 'La persona fue registrada.');
        this.cancelEdit();
      },
      error: (response) => this.error.set(this.getErrorMessage(response, 'No fue posible guardar los cambios.')),
    });
  }

  edit(persona: Persona): void {
    this.editingPersona.set(persona);
    this.personaForm.setValue({ nombre: persona.nombre, edad: persona.edad });
    this.success.set(null);
  }

  cancelEdit(): void {
    this.editingPersona.set(null);
    this.personaForm.reset({ nombre: '', edad: 0 });
  }

  showDetails(id: number): void {
    this.error.set(null);
    this.personaApi.getById(id).subscribe({
      next: (persona) => this.detailPersona.set(persona),
      error: (response) => this.error.set(this.getErrorMessage(response, 'No fue posible consultar la persona.')),
    });
  }

  requestDeletion(persona: Persona): void {
    this.pendingDeletion.set(persona);
  }

  delete(): void {
    const persona = this.pendingDeletion();
    if (!persona) {
      return;
    }

    this.deletingId.set(persona.id);
    this.error.set(null);

    this.personaApi
      .delete(persona.id)
      .pipe(finalize(() => this.deletingId.set(null)))
      .subscribe({
        next: () => {
          this.personas.update((personas) => personas.filter((current) => current.id !== persona.id));
          this.pendingDeletion.set(null);
          this.success.set('La persona fue eliminada.');
          if (this.editingPersona()?.id === persona.id) {
            this.cancelEdit();
          }
        },
        error: (response) => this.error.set(this.getErrorMessage(response, 'No fue posible eliminar la persona.')),
      });
  }

  closeDetail(): void {
    this.detailPersona.set(null);
  }

  dismissMessages(): void {
    this.error.set(null);
    this.success.set(null);
  }

  private getErrorMessage(response: { error?: { detail?: string } }, fallback: string): string {
    return response.error?.detail ?? fallback;
  }
}
