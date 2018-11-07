/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NewLocalTestModule } from '../../../test.module';
import { PurchasePendingDeleteDialogComponent } from 'app/entities/purchase-pending/purchase-pending-delete-dialog.component';
import { PurchasePendingService } from 'app/entities/purchase-pending/purchase-pending.service';

describe('Component Tests', () => {
    describe('PurchasePending Management Delete Component', () => {
        let comp: PurchasePendingDeleteDialogComponent;
        let fixture: ComponentFixture<PurchasePendingDeleteDialogComponent>;
        let service: PurchasePendingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchasePendingDeleteDialogComponent]
            })
                .overrideTemplate(PurchasePendingDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchasePendingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchasePendingService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
