import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IContractType, ContractType } from 'app/shared/model/contract-type.model';
import { ContractTypeService } from './contract-type.service';

@Component({
  selector: 'jhi-contract-type-update',
  templateUrl: './contract-type-update.component.html'
})
export class ContractTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]]
  });

  constructor(protected contractTypeService: ContractTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractType }) => {
      this.updateForm(contractType);
    });
  }

  updateForm(contractType: IContractType): void {
    this.editForm.patchValue({
      id: contractType.id,
      name: contractType.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractType = this.createFromForm();
    if (contractType.id !== undefined) {
      this.subscribeToSaveResponse(this.contractTypeService.update(contractType));
    } else {
      this.subscribeToSaveResponse(this.contractTypeService.create(contractType));
    }
  }

  private createFromForm(): IContractType {
    return {
      ...new ContractType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractType>>): void {
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
}
