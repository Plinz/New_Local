/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchasePendingUpdateComponent } from 'app/entities/purchase-pending/purchase-pending-update.component';
import { PurchasePendingService } from 'app/entities/purchase-pending/purchase-pending.service';
import { PurchasePending } from 'app/shared/model/purchase-pending.model';

describe('Component Tests', () => {
    describe('PurchasePending Management Update Component', () => {
        let comp: PurchasePendingUpdateComponent;
        let fixture: ComponentFixture<PurchasePendingUpdateComponent>;
        let service: PurchasePendingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchasePendingUpdateComponent]
            })
                .overrideTemplate(PurchasePendingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchasePendingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchasePendingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PurchasePending(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchasePending = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PurchasePending();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.purchasePending = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
