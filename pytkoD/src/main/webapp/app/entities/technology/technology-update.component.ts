import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITechnology, Technology } from 'app/shared/model/technology.model';
import { TechnologyService } from './technology.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';
import { IResource } from 'app/shared/model/resource.model';
import { ResourceService } from 'app/entities/resource/resource.service';

type SelectableEntity = IEmployee | IResource;

@Component({
  selector: 'jhi-technology-update',
  templateUrl: './technology-update.component.html'
})
export class TechnologyUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  resources: IResource[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    employyes: [],
    resources: []
  });

  constructor(
    protected technologyService: TechnologyService,
    protected employeeService: EmployeeService,
    protected resourceService: ResourceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technology }) => {
      this.updateForm(technology);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

      this.resourceService.query().subscribe((res: HttpResponse<IResource[]>) => (this.resources = res.body || []));
    });
  }

  updateForm(technology: ITechnology): void {
    this.editForm.patchValue({
      id: technology.id,
      name: technology.name,
      employyes: technology.employyes,
      resources: technology.resources
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const technology = this.createFromForm();
    if (technology.id !== undefined) {
      this.subscribeToSaveResponse(this.technologyService.update(technology));
    } else {
      this.subscribeToSaveResponse(this.technologyService.create(technology));
    }
  }

  private createFromForm(): ITechnology {
    return {
      ...new Technology(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      employyes: this.editForm.get(['employyes'])!.value,
      resources: this.editForm.get(['resources'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnology>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableEntity[], option: SelectableEntity): SelectableEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
